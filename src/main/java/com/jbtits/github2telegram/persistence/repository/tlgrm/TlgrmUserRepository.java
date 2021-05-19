package com.jbtits.github2telegram.persistence.repository.tlgrm;

import com.jbtits.github2telegram.persistence.entity.tlgrm.TlgrmUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TlgrmUserRepository extends JpaRepository<TlgrmUser, Long> {

  List<TlgrmUser> findAllByTlgrmUserIdIn(Iterable<Long> userIds);

  Optional<TlgrmUser> findByTlgrmUserId(long userId);
}
