package com.jbtits.github2telegram.domain.event;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NewUrlMessageEvent {
  @NonNull
  private final String url;
  @NonNull
  private final String username;
  @NonNull
  private final String chatId;
}
