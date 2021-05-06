package com.jbtits.github2telegram.domain.dto.announce;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
abstract class AbstractAnnounce {
  private final long chatId;
}
