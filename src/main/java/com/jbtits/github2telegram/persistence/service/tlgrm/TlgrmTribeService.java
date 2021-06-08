package com.jbtits.github2telegram.persistence.service.tlgrm;

import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.domain.exception.tlgrm.cfg.TlgrmChatNotFoundException;
import com.jbtits.github2telegram.domain.exception.tlgrm.cfg.TlgrmFellowNotFoundException;
import com.jbtits.github2telegram.domain.exception.tlgrm.cfg.TlgrmUserNotFoundException;
import com.jbtits.github2telegram.persistence.entity.Fellow;
import com.jbtits.github2telegram.persistence.entity.Tribe;
import com.jbtits.github2telegram.persistence.entity.tlgrm.TlgrmChat;
import com.jbtits.github2telegram.persistence.repository.TribeRepository;
import com.jbtits.github2telegram.persistence.service.TeamService;
import com.jbtits.github2telegram.persistence.service.TribeService;
import com.jbtits.github2telegram.util.CollectionUtils;
import com.jbtits.github2telegram.util.RandomUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TlgrmTribeService implements TribeService<TlgrmChatContext, TlgrmUserContext> {

  public static final int MAX_REVIEWERS_COUNT = 2;

  private final TlgrmChatService tlgrmChatService;
  private final TlgrmUserService tlgrmUserService;
  private final TeamService<TlgrmUserContext> teamService;
  private final TribeRepository tribeRepository;

  @Override
  public Optional<Tribe> findById(final long id) {
    return tribeRepository.findById(id);
  }

  @Override
  public void save(@NonNull TribeConfiguration<TlgrmChatContext, TlgrmUserContext> configuration) {
    final TlgrmChatContext context = configuration.getContext();
    final TlgrmChat tlgrmChat = this.tlgrmChatService.findByTlgrmChatId(context.getChatId())
        .orElseGet(() -> this.tlgrmChatService.generate(context));
    if (tlgrmChat.getTribe() == null) {
      tlgrmChat.setTribe(new Tribe());
    }
    final Tribe tribe = tlgrmChat.getTribe();
    tribe.setName(configuration.getName());
    this.teamService.create(configuration.getTeams(), tribe);
    tlgrmChat.setTribe(tribe);
    this.tribeRepository.save(tribe);
    this.tlgrmChatService.save(tlgrmChat);
  }

  /**
   *
   * @param userContext
   * @return
   *
   * @throws TlgrmChatNotFoundException if no chat for context is found
   * @throws TlgrmUserNotFoundException if no user for context is found
   */
  @Override
  @Transactional(readOnly = true)
  public @NonNull List<Fellow> findReviewers(@NonNull final TlgrmUserContext userContext) {
    final var tlgrmChat = this.tlgrmChatService.findByTlgrmChatId(userContext.getChatId())
        .orElseThrow(
            () -> new TlgrmChatNotFoundException("TlgrmChat for tlgrm_chat_id " + userContext.getChatId()
                + " not found"));
    final var tribe = tlgrmChat.getTribe();
    if (tribe == null) {
      throw new IllegalStateException("Every TlgrmChat must have Tribe");
    }
    final var fellows = tribe.getTeams().stream()
        .filter(Objects::nonNull)
        .flatMap(team -> team.getFellows().stream())
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    final var tlgrmUsersForFellows = this.tlgrmUserService.findTlgrmUsersForFellows(fellows);
    final var authorTlgrmUser = this.tlgrmUserService
        .findByTlgrmUserId(userContext.getUserId())
        .orElseThrow(() -> new TlgrmUserNotFoundException("TlgrmUser for tlgrm_user_id " + userContext.getUserId()
            + " not found"));
    final var authorFellow = fellows.stream()
        .filter(fellow
            -> Objects.equals(tlgrmUsersForFellows.get(fellow).getTlgrmUserId(), authorTlgrmUser.getTlgrmUserId()))
        .findFirst()
        .orElseThrow(() -> new TlgrmFellowNotFoundException("No Fellow for TlgrmUser"));
    fellows.removeIf(fellow
        -> Objects.equals(tlgrmUsersForFellows.get(fellow).getTlgrmUserId(), authorTlgrmUser.getTlgrmUserId()));
    final var fellowsInTheSameTeam = fellows.stream()
        .filter(fellow -> {
          final var team = fellow.getTeam();
          if (team == null) {
            throw new IllegalStateException("Fellow doesn't have the team");
          }
          return team.getName().equals(authorFellow.getTeam().getName());
        })
        .collect(Collectors.toList());
    final var fellowsInOtherTeams = fellows.stream()
        .filter(fellow -> {
          final var team = fellow.getTeam();
          if (team == null) {
            throw new IllegalStateException("Fellow doesn't have the team");
          }
          return !team.getName().equals(authorFellow.getTeam().getName());
        })
        .collect(Collectors.toList());
    final var availableReviewersInTheSameTeam = RandomUtils.getRandoms(fellowsInTheSameTeam,
        MAX_REVIEWERS_COUNT);
    final var availableReviewersInOtherTeams = RandomUtils.getRandoms(fellowsInOtherTeams,
        MAX_REVIEWERS_COUNT);
    return CollectionUtils.combineOneAfterOne(availableReviewersInTheSameTeam, availableReviewersInOtherTeams).stream()
        .limit(MAX_REVIEWERS_COUNT)
        .collect(Collectors.toList());
  }


}
