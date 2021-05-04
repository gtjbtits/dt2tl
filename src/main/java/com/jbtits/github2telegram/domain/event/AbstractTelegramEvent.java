package com.jbtits.github2telegram.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
abstract class AbstractTelegramEvent {
  private final long chatId;
}
