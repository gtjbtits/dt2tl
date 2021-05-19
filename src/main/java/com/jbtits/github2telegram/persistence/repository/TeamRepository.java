package com.jbtits.github2telegram.persistence.repository;

import com.jbtits.github2telegram.persistence.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
