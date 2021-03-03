package com.jbtits.github2telegram.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MentionAllEvent {
  private final String chatId;
}
