package com.jbtits.github2telegram.persistence.service.tlgrm;

import com.jbtits.github2telegram.domain.dto.cfg.TeamConfiguration;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.persistence.entity.Team;
import com.jbtits.github2telegram.persistence.entity.Tribe;
import com.jbtits.github2telegram.persistence.repository.TeamRepository;
import com.jbtits.github2telegram.persistence.service.FellowService;
import com.jbtits.github2telegram.persistence.service.TeamService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TlgrmTeamService implements TeamService<TlgrmUserContext> {

  private final TlgrmChatService tlgrmChatService;
  private final FellowService<TlgrmUserContext> fellowService;
  private final TeamRepository teamRepository;

  @Override
  public void create(@NonNull List<TeamConfiguration<TlgrmUserContext>> teamConfigurations, @NonNull Tribe tribe) {
    if (tribe.getTeams() == null) {
      tribe.setTeams(new ArrayList<>());
    }
    final List<Team> teams = tribe.getTeams();
    final Set<String> teamNames = teamConfigurations.stream()
        .map(TeamConfiguration::getName)
        .collect(Collectors.toSet());
    if (teamNames.size() != teamConfigurations.size()) {
      throw new IllegalArgumentException("Team names must be unique inside tribe");
    }
    final List<Team> retainTeams = new ArrayList<>();
    teamConfigurations.forEach(teamConfiguration -> {
      final var retainTeam = this.save(teamConfiguration, tribe);
      retainTeams.add(retainTeam);
    });
    teams.clear();
    teams.addAll(retainTeams);
  }

  @Override
  public void drop(final @NonNull TlgrmChatContext context) {
    tlgrmChatService.findByTlgrmChatId(context.getChatId()).ifPresent(tlgrmChat -> {
      final var tribe = tlgrmChat.getTribe();
      if (tribe == null) {
        throw new IllegalStateException("TlgrmChat doesn't have a Tribe");
      }
      Optional.ofNullable(tribe.getTeams()).ifPresent(this.teamRepository::deleteAll);
    });
  }

  @Override
  public List<Team> findAll() {
    return teamRepository.findAll();
  }

  private Team save(@NonNull TeamConfiguration<TlgrmUserContext> teamConfiguration, @NonNull Tribe tribe) {
    final Map<String, Team> teamsByName = tribe.getTeams().stream()
        .collect(Collectors.toMap(Team::getName, team -> team));
    final Team team;
    if (teamsByName.containsKey(teamConfiguration.getName())) {
      team = teamsByName.get(teamConfiguration.getName());
    } else {
      team = new Team();
      tribe.getTeams().add(team);
    }
    team.setName(teamConfiguration.getName());
    team.setTribe(tribe);
    this.fellowService.save(teamConfiguration.getFellows(), team);
    return this.teamRepository.save(team);
  }
}
