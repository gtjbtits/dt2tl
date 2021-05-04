package com.jbtits.github2telegram.domain.event;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class NewUrlMessageEvent extends AbstractTelegramEvent {

  @NonNull
  private final String url;
  @NonNull
  private final String username;

  public NewUrlMessageEvent(long chatId, String url, String username) {
    super(chatId);
    this.url = url;
    this.username = username;
  }
}
