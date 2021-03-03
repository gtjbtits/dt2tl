package com.jbtits.github2telegram.persistence.service;

import com.jbtits.github2telegram.persistence.entity.Developer;
import com.jbtits.github2telegram.persistence.repository.DeveloperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DeveloperService {

  private static final Random RANDOM = new Random();

  private final DeveloperRepository repository;

  @Transactional(readOnly = true)
  public Set<Developer> findReviewers(String username) {
    final Set<Developer> reviewers = new HashSet<>();
    repository.findDeveloperByUsername(username).ifPresent(developer -> {
      final Developer developerFromTeam = this.findRandomDeveloperInHisTeamExceptHimSelf(developer).orElseThrow();
      final Developer developerFromOtherTeams = this.findRandomDeveloperInOtherTeams(developer).orElseThrow();
      reviewers.add(developerFromTeam);
      reviewers.add(developerFromOtherTeams);
    });
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

  private Optional<Developer> findRandomDeveloperInOtherTeams(Developer developer) {
    final List<Developer> developers = this.repository.findAllByTeamNot(developer.getTeam());
    return this.getRandomDeveloper(developers);
  }

  private Optional<Developer> getRandomDeveloper(List<Developer> developers) {
    if (developers.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(developers.get(RANDOM.nextInt(developers.size())));
  }
}
