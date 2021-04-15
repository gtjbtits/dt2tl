package com.jbtits.github2telegram.persistence.repository;

import com.jbtits.github2telegram.persistence.entity.Fellow;
import com.jbtits.github2telegram.persistence.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeveloperRepository extends JpaRepository<Fellow, Long> {

  List<Fellow> findAllByTeamNot(Team team);
}
