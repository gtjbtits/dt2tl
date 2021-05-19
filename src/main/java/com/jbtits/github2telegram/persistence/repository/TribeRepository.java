package com.jbtits.github2telegram.persistence.repository;

import com.jbtits.github2telegram.persistence.entity.Tribe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TribeRepository extends JpaRepository<Tribe, Long> {
}
