package com.jbtits.github2telegram.persistence.repository.tlgrm;

import com.jbtits.github2telegram.persistence.entity.Fellow;
import com.jbtits.github2telegram.persistence.entity.Tribe;
import com.jbtits.github2telegram.persistence.entity.tlgrm.TlgrmUserFellow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TlgrmUserFellowRepository extends JpaRepository<TlgrmUserFellow, Long> {

  List<TlgrmUserFellow> findAllByFellowIn(Iterable<Fellow> fellows);

  List<TlgrmUserFellow> findAllByTribe(Tribe tribe);
}
