package com.jbtits.github2telegram.domain.dto.announce;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CodeReviewAnnounce {
  @NonNull
  private final String from;
  @NonNull
  private final String[] to;
  @NonNull
  private final String url;
  private final long chatId;
}
