package com.jbtits.github2telegram.domain.event;

import lombok.Getter;

@Getter
public class MentionAllEvent extends AbstractTelegramEvent {

  public MentionAllEvent(long chatId) {
    super(chatId);
  }
}
