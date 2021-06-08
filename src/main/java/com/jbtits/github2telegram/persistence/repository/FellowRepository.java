package com.jbtits.github2telegram.persistence.repository;

import com.jbtits.github2telegram.persistence.entity.Fellow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FellowRepository extends JpaRepository<Fellow, Long> {
}
