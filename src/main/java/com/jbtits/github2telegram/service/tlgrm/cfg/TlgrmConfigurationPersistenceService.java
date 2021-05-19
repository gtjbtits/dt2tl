package com.jbtits.github2telegram.service.tlgrm.cfg;

import com.jbtits.github2telegram.domain.dto.cfg.FellowConfiguration;
import com.jbtits.github2telegram.domain.dto.cfg.TeamConfiguration;
import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.persistence.entity.Fellow;
import com.jbtits.github2telegram.persistence.entity.Team;
import com.jbtits.github2telegram.persistence.entity.Tribe;
import com.jbtits.github2telegram.persistence.entity.tlgrm.TlgrmChat;
import com.jbtits.github2telegram.persistence.entity.tlgrm.TlgrmUser;
import com.jbtits.github2telegram.persistence.service.TeamService;
import com.jbtits.github2telegram.persistence.service.TribeService;
import com.jbtits.github2telegram.persistence.service.tlgrm.TlgrmChatService;
import com.jbtits.github2telegram.persistence.service.tlgrm.TlgrmUserFellowService;
import com.jbtits.github2telegram.persistence.service.tlgrm.TlgrmUserService;
import com.jbtits.github2telegram.service.cfg.ConfigurationPersistenceService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TlgrmConfigurationPersistenceService
    implements ConfigurationPersistenceService<TlgrmChatContext, TlgrmUserContext> {

  private final TribeService<TlgrmChatContext, TlgrmUserContext> tribeService;
  private final TlgrmChatService tlgrmChatService;
  private final TlgrmUserService tlgrmUserService;
  private final TlgrmUserFellowService tlgrmUserFellowService;
  private final TeamService<TlgrmUserContext> teamService;

  @Override
  @Transactional(readOnly = true)
  public Optional<TribeConfiguration<TlgrmChatContext, TlgrmUserContext>> get(@NonNull TlgrmChatContext context) {
    final Optional<TlgrmChat> tlgrmChatOpt = this.tlgrmChatService.findByTlgrmChatId(context.getChatId());
    if (tlgrmChatOpt.isPresent()) {
      final var tlgrmChat = tlgrmChatOpt.get();
      final var tribe = tlgrmChat.getTribe();
      if (tribe == null) {
        log.error("TlgrmChat with id {} hasn't linked Tribe", tlgrmChat.getId());
        throw new IllegalStateException("TlgrmChat hasn't linked Tribe");
      }
      final List<Team> teams = tribe.getTeams();
      if (teams == null) {
        log.error("Tribe with id {} has null TeamSet", tribe.getId());
        throw new IllegalStateException("Tribe has null TeamSet");
      }
      final List<Fellow> tribeFellows = teams.stream()
          .filter(team -> Objects.nonNull(team.getFellows()))
          .flatMap(team -> team.getFellows().stream())
          .collect(Collectors.toList());
      final Map<Fellow, TlgrmUser> fellowsVsTlgrmUsers = this.tlgrmUserService.findTlgrmUsersForFellows(tribeFellows);
      return Optional.of(this.buildTribeConfiguration(tlgrmChat, fellowsVsTlgrmUsers));
    }
    return Optional.empty();
  }

  @Override
  @Transactional(propagation = Propagation.NEVER)
  public void save(@NonNull TribeConfiguration<TlgrmChatContext, TlgrmUserContext> configuration) {
    this.tlgrmUserFellowService.drop(configuration.getContext());
    this.teamService.drop(configuration.getContext());
    this.tribeService.save(configuration);
  }

  @Override
  public void deactivate(final @NonNull TlgrmChatContext context) {
    this.tlgrmChatService.findByTlgrmChatId(context.getChatId()).ifPresent(tlgrmChat -> {
      final var tribe = tlgrmChat.getTribe();
      if (tribe == null) {
        throw new IllegalStateException("Telegram Chat hasn't Tribe, but it couldn't be");
      }
      this.tribeService.deactivate(tribe);
    });
  }

  @Override
  public void activate(final @NonNull TlgrmChatContext context) {
    this.tlgrmChatService.findByTlgrmChatId(context.getChatId()).ifPresent(tlgrmChat -> {
      final var tribe = tlgrmChat.getTribe();
      if (tribe == null) {
        throw new IllegalStateException("Telegram Chat hasn't Tribe, but it couldn't be");
      }
      this.tribeService.activate(tribe);
    });
  }

  @NonNull
  private TribeConfiguration<TlgrmChatContext, TlgrmUserContext> buildTribeConfiguration(
      @NonNull TlgrmChat tlgrmChat,
      @NonNull Map<Fellow, TlgrmUser> fellowsVsTlgrmUsers) {
    final TlgrmChatContext tlgrmChatContext = new TlgrmChatContext(tlgrmChat.getTlgrmChatId());
    final Tribe tribe = tlgrmChat.getTribe();
    final TribeConfiguration<TlgrmChatContext, TlgrmUserContext> tribeConfiguration
        = new TribeConfiguration<>(tlgrmChatContext, tribe.getName());
    tribe.getTeams().forEach(team -> {
      final var teamConfiguration
          = this.buildTeamConfiguration(team, tlgrmChat, fellowsVsTlgrmUsers);
      tribeConfiguration.getTeams().add(teamConfiguration);
    });
    return tribeConfiguration;
  }

  @NonNull
  private TeamConfiguration<TlgrmUserContext> buildTeamConfiguration(
      @NonNull Team team,
      @NonNull TlgrmChat tlgrmChat,
      @NonNull Map<Fellow, TlgrmUser> fellowsVsTlgrmUsers) {
    final TeamConfiguration<TlgrmUserContext> teamConfiguration = new TeamConfiguration<>(team.getName());
    final List<Fellow> fellows = team.getFellows();
    if (fellows == null) {
      log.error("Team with id={} has null fellows", team.getId());
      throw new IllegalStateException("Team fellows is null");
    }
    fellows.forEach(fellow -> {
      final var fellowConfiguration
          = this.buildFellowConfiguration(tlgrmChat, fellow, fellowsVsTlgrmUsers.get(fellow));
      teamConfiguration.getFellows().add(fellowConfiguration);
    });
    return teamConfiguration;
  }

  @NonNull
  private FellowConfiguration<TlgrmUserContext> buildFellowConfiguration(
      @NonNull TlgrmChat tlgrmChat,
      @NonNull Fellow fellow,
      @NonNull TlgrmUser tlgrmUser) {
    final var tlgrmUserContext
        = new TlgrmUserContext(tlgrmChat.getTlgrmChatId(), tlgrmUser.getTlgrmUserId());
    return new FellowConfiguration<>(tlgrmUserContext, fellow.getName());
  }
}
