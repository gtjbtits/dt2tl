package com.jbtits.github2telegram.domain.dto.tlgrm;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class TlgrmCallbackContext extends TlgrmUserContext {

  @NonNull
  private final String callbackId;

  public TlgrmCallbackContext(long chatId, long userId, @NonNull String callbackId) {
    super(chatId, userId);
    this.callbackId = callbackId;
  }
}
