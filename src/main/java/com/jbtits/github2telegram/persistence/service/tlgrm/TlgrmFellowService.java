package com.jbtits.github2telegram.persistence.service.tlgrm;

import com.jbtits.github2telegram.domain.dto.cfg.FellowConfiguration;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.persistence.entity.Fellow;
import com.jbtits.github2telegram.persistence.entity.Team;
import com.jbtits.github2telegram.persistence.entity.tlgrm.TlgrmUser;
import com.jbtits.github2telegram.persistence.entity.tlgrm.TlgrmUserFellow;
import com.jbtits.github2telegram.persistence.repository.FellowRepository;
import com.jbtits.github2telegram.persistence.service.FellowService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TlgrmFellowService implements FellowService<TlgrmUserContext> {

  private final TlgrmUserService tlgrmUserService;
  private final FellowRepository fellowRepository;

  @Override
  public void save(@NonNull List<FellowConfiguration<TlgrmUserContext>> fellowConfigurations, @NonNull Team team) {
    if (team.getFellows() == null) {
      team.setFellows(new ArrayList<>());
    }
    final Set<Long> userIds = fellowConfigurations.stream()
        .map(FellowConfiguration::getContext)
        .map(TlgrmUserContext::getUserId)
        .collect(Collectors.toSet());
    this.validateFellowConfigurations(fellowConfigurations, userIds);
    final Map<Long, TlgrmUser> tlgrmUserMap = this.tlgrmUserService.findAllByTelegramIds(userIds).stream()
        .collect(Collectors.toMap(TlgrmUser::getTlgrmUserId, tlgrmUser -> tlgrmUser));
    final List<Fellow> retainedFellows = new ArrayList<>();
    final Set<Fellow> orphanFellows = new HashSet<>(team.getFellows());
    team.getFellows().clear();
    fellowConfigurations.forEach(fellowConfiguration -> {
      final var retainedFellow = this.save(fellowConfiguration, team, tlgrmUserMap);
      retainedFellows.add(retainedFellow);
      orphanFellows.remove(retainedFellow);
    });
    team.getFellows().clear();
    team.getFellows().addAll(retainedFellows);
    this.tlgrmUserService.removeOrphans(new ArrayList<>(orphanFellows));
  }

  private Fellow save(
      @NonNull FellowConfiguration<TlgrmUserContext> fellowConfiguration,
      @NonNull Team team,
      @NonNull Map<Long, TlgrmUser> tlgrmUserMap) {
    final TlgrmUserContext context = fellowConfiguration.getContext();
    final TlgrmUser tlgrmUser = tlgrmUserMap.getOrDefault(context.getUserId(), this.generate(context));
    if (tlgrmUser.getTlgrmUserFellows() == null) {
      tlgrmUser.setTlgrmUserFellows(new ArrayList<>());
    }
    final Optional<Fellow> persistedFellow = this.getTlgrmUserFellowInSpecifiedTeam(team, tlgrmUser);
    final Fellow fellow;
    if (persistedFellow.isPresent()) {
      fellow = persistedFellow.get();
    } else {
      fellow = new Fellow();
      team.getFellows().add(fellow);
      final TlgrmUserFellow tlgrmUserFellow = new TlgrmUserFellow();
      tlgrmUserFellow.setFellow(fellow);
      tlgrmUserFellow.setTlgrmUser(tlgrmUser);
      tlgrmUserFellow.setTribe(team.getTribe());
      tlgrmUser.getTlgrmUserFellows().add(tlgrmUserFellow);
    }
    fellow.setName(fellowConfiguration.getName());
    fellow.setTeam(team);

    final var savedFellow = this.fellowRepository.save(fellow);
    this.tlgrmUserService.save(tlgrmUser);
    return savedFellow;
  }

  @NonNull
  private Optional<Fellow> getTlgrmUserFellowInSpecifiedTeam(@NonNull final Team team, @NonNull final TlgrmUser tlgrmUser) {
    if (tlgrmUser.getTlgrmUserFellows() == null || team.getFellows() == null) {
      throw new IllegalArgumentException("Fellow sets can't be null before intersect");
    }
    final Set<Fellow> tlgrmUserFellowsInCurrentTeam = tlgrmUser.getTlgrmUserFellows().stream()
        .map(TlgrmUserFellow::getFellow).collect(Collectors.toSet());
    tlgrmUserFellowsInCurrentTeam.retainAll(team.getFellows());
    if (tlgrmUserFellowsInCurrentTeam.size() > 1) {
      log.error("Team with id={} has multiple fellows with the same tlgrmUserId={}",
          team.getId(), tlgrmUser.getTlgrmUserId());
      throw new IllegalStateException("Team has multiple fellows with the same tlgrmUserId");
    }
    return tlgrmUserFellowsInCurrentTeam.stream().findFirst();
  }

  @NonNull
  private TlgrmUser generate(@NonNull final TlgrmUserContext context) {
    final TlgrmUser tlgrmUser = new TlgrmUser();
    tlgrmUser.setTlgrmUserId(context.getUserId());
    return tlgrmUser;
  }

  private void validateFellowConfigurations(
      final @NonNull List<FellowConfiguration<TlgrmUserContext>> fellowConfigurations,
      final @NonNull Set<Long> cfgUserIds) {
    if (fellowConfigurations.size() != cfgUserIds.size()) {
      throw new IllegalStateException("TlgrmUsers in tribe must be unique");
    }
  }
}
