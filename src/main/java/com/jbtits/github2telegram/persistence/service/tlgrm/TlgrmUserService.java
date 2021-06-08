package com.jbtits.github2telegram.persistence.service.tlgrm;

import com.jbtits.github2telegram.persistence.entity.Fellow;
import com.jbtits.github2telegram.persistence.entity.tlgrm.TlgrmUser;
import com.jbtits.github2telegram.persistence.entity.tlgrm.TlgrmUserFellow;
import com.jbtits.github2telegram.persistence.repository.tlgrm.TlgrmUserFellowRepository;
import com.jbtits.github2telegram.persistence.repository.tlgrm.TlgrmUserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TlgrmUserService {

  private final TlgrmUserRepository tlgrmUserRepository;
  private final TlgrmUserFellowRepository tlgrmUserFellowRepository;

  public void save(@NonNull TlgrmUser tlgrmUser) {
    tlgrmUserRepository.save(tlgrmUser);
  }

  @Transactional
  public Optional<TlgrmUser> findByTlgrmUserId(long userId) {
    return this.tlgrmUserRepository.findByTlgrmUserId(userId);
  }


  @NonNull
  @Transactional(readOnly = true)
  public List<TlgrmUser> findAllByTelegramIds(@NonNull Set<Long> userIds) {
    return tlgrmUserRepository.findAllByTlgrmUserIdIn(userIds);
  }

  @NonNull
  @Transactional(readOnly = true)
  public List<TlgrmUser> findAll() {
    return tlgrmUserRepository.findAll();
  }

  @NonNull
  @Transactional(readOnly = true)
  public Map<Fellow, TlgrmUser> findTlgrmUsersForFellows(@NonNull List<Fellow> fellows) {
    final List<TlgrmUserFellow> tlgrmUserFellows = this.tlgrmUserFellowRepository.findAllByFellowIn(fellows);
    if (fellows.size() != tlgrmUserFellows.size()) {
      log.error("Fellows and TlgrmUsers counts mismatch. They must be equal. Fellows: {}, TlgrmUsers: {}",
          fellows, tlgrmUserFellows);
      throw new IllegalStateException("Fellows and TlgrmUsers counts mismatch. They must be equal");
    }
    return tlgrmUserFellows.stream()
        .collect(Collectors.toMap(TlgrmUserFellow::getFellow, TlgrmUserFellow::getTlgrmUser));
  }

  public void removeOrphans(@NonNull final List<Fellow> orphanFellows) {
    final Map<Fellow, TlgrmUser> fellowsVsTlgrmUsers = this.findTlgrmUsersForFellows(orphanFellows);
    fellowsVsTlgrmUsers.values().forEach(tlgrmUser -> {
          final Set<Fellow> tlgrmUserFellows = tlgrmUser.getTlgrmUserFellows().stream()
              .map(TlgrmUserFellow::getFellow)
              .collect(Collectors.toSet());
          tlgrmUserFellows.removeAll(orphanFellows);
          if (tlgrmUserFellows.isEmpty()) {
            this.tlgrmUserRepository.delete(tlgrmUser);
          }
    });
  }
}
