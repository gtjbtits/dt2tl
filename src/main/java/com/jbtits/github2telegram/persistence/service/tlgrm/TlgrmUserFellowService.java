package com.jbtits.github2telegram.persistence.service.tlgrm;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.persistence.entity.Fellow;
import com.jbtits.github2telegram.persistence.entity.tlgrm.TlgrmUserFellow;
import com.jbtits.github2telegram.persistence.repository.tlgrm.TlgrmUserFellowRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TlgrmUserFellowService {

  private final TlgrmChatService tlgrmChatService;
  private final TlgrmUserFellowRepository tlgrmUserFellowRepository;

  public List<TlgrmUserFellow> findTlgrmUserFellowsForFellows(final @NonNull Iterable<Fellow> fellows) {
    return this.tlgrmUserFellowRepository.findAllByFellowIn(fellows);
  }

  public void drop(final @NonNull TlgrmChatContext context) {
    this.tlgrmChatService.findByTlgrmChatId(context.getChatId()).ifPresent(tlgrmChat -> {
      final var tribe = tlgrmChat.getTribe();
      if (tribe == null) {
        throw new IllegalStateException("TlgrmChat doesn't have a Tribe");
      }
      final var allByTribe = this.tlgrmUserFellowRepository.findAllByTribe(tribe);
      this.tlgrmUserFellowRepository.deleteAll(allByTribe);
    });
  }
}
