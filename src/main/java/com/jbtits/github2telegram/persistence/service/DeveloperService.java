package com.jbtits.github2telegram.persistence.service;

import com.jbtits.github2telegram.persistence.entity.Fellow;
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
  public Set<Fellow> findReviewers(String username) {
    final Set<Fellow> reviewers = new HashSet<>();
    throw new RuntimeException("Fix reviewers search strategy");
//    repository.findDeveloperByUsername(username).ifPresent(developer -> {
//      final Fellow fellowFromTeam = this.findRandomDeveloperInHisTeamExceptHimSelf(developer).orElseThrow();
//      final Fellow fellowFromOtherTeams = this.findRandomDeveloperInOtherTeams(developer).orElseThrow();
//      reviewers.add(fellowFromTeam);
//      reviewers.add(fellowFromOtherTeams);
//    });
//    return reviewers;
  }

  @Transactional(readOnly = true)
  public List<Fellow> findAll() {
    return this.repository.findAll();
  }

  private Optional<Fellow> findRandomDeveloperInHisTeamExceptHimSelf(Fellow fellow) {
    throw new RuntimeException("TELEGram @username here");
//    final List<Fellow> fellows =
//        this.repository.findAllByTeamAndUsernameNot(fellow.getTeam(), fellow.getName());
//    return this.getRandomDeveloper(fellows);
  }

  private Optional<Fellow> findRandomDeveloperInOtherTeams(Fellow fellow) {
    final List<Fellow> fellows = this.repository.findAllByTeamNot(fellow.getTeam());
    return this.getRandomDeveloper(fellows);
  }

  private Optional<Fellow> getRandomDeveloper(List<Fellow> fellows) {
    if (fellows.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(fellows.get(RANDOM.nextInt(fellows.size())));
  }
}
