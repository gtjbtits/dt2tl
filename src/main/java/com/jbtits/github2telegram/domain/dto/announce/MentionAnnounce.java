package com.jbtits.github2telegram.domain.dto.announce;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MentionAnnounce {
  @NonNull
  private final String[] to;
  private final long chatId;
}
