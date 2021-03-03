package com.jbtits.github2telegram.persistence.repository;

import com.jbtits.github2telegram.persistence.entity.Developer;
import com.jbtits.github2telegram.persistence.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {

  Optional<Developer> findDeveloperByUsername(String username);

  List<Developer> findAllByTeamAndUsernameNot(Team team, String username);

  List<Developer> findAllByTeamNot(Team team);
}
