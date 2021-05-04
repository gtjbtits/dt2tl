package com.jbtits.github2telegram.domain.event;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class NewConfigEvent extends AbstractTelegramEvent {

  @NonNull
  private final String fileId;

  public NewConfigEvent(long chatId, String fileId) {
    super(chatId);
    this.fileId = fileId;
  }
}
