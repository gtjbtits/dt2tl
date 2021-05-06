package com.jbtits.github2telegram.domain.dto.announce;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class TextAnnounce extends AbstractAnnounce {

  private final String text;

  public TextAnnounce(long chatId, @NonNull String text) {
    super(chatId);
    this.text = text;
  }
}
