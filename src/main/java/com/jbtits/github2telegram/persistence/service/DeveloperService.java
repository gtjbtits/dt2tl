package com.jbtits.github2telegram.persistence.service;

import com.jbtits.github2telegram.persistence.entity.Developer;
import com.jbtits.github2telegram.persistence.repository.DeveloperRepository;
import com.jbtits.github2telegram.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DeveloperService {

  private final DeveloperRepository repository;

  @Transactional(readOnly = true)
  public Set<Developer> findReviewers(String username) {
    final Set<Developer> reviewers = new HashSet<>();
    repository.findDeveloperByUsername(username).ifPresent(developer ->
      this.findRandomDeveloperInHisTeamExceptHimSelf(developer).ifPresentOrElse(d -> {
        final List<Developer> developersFromOtherTeams = this.findRandomDevelopersInOtherTeams(developer, 1);
        reviewers.add(d);
        reviewers.addAll(developersFromOtherTeams);
      }, () -> {
        final List<Developer> developersFromOtherTeams = this.findRandomDevelopersInOtherTeams(developer, 2);
        reviewers.addAll(developersFromOtherTeams);
      })
    );
    return reviewers;
  }

  @Transactional(readOnly = true)
  public List<Developer> findAll() {
    return this.repository.findAll();
  }

  private Optional<Developer> findRandomDeveloperInHisTeamExceptHimSelf(Developer developer) {
    final List<Developer> developers =
        this.repository.findAllByTeamAndUsernameNot(developer.getTeam(), developer.getUsername());
    return this.getRandomDeveloper(developers);
  }

  private List<Developer> findRandomDevelopersInOtherTeams(Developer developer, int amount) {
    final List<Developer> developers = this.repository.findAllByTeamNot(developer.getTeam());
    return RandomUtil.getRandoms(developers, amount);
  }

  private Optional<Developer> getRandomDeveloper(List<Developer> developers) {
    if (developers.isEmpty()) {
      return Optional.empty();
    }
    return RandomUtil.getRandom(developers);
  }
}
