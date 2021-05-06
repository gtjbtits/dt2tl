package com.jbtits.github2telegram.domain.dto.announce;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class CodeReviewAnnounce extends AbstractAnnounce {
  @NonNull
  private final String from;
  @NonNull
  private final String[] to;
  @NonNull
  private final String url;

  public CodeReviewAnnounce(long chatId, @NonNull String from, @NonNull String[] to, @NonNull String url) {
    super(chatId);
    this.from = from;
    this.to = to;
    this.url = url;
  }
}
