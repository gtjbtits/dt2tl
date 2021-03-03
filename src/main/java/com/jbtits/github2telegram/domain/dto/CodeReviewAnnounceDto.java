package com.jbtits.github2telegram.domain.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CodeReviewAnnounceDto {
  @NonNull
  private final String from;
  @NonNull
  private final String[] to;
  @NonNull
  private final String url;
  @NonNull
  private final String chatId;
}
