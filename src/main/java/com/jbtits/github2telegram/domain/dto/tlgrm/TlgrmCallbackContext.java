package com.jbtits.github2telegram.domain.dto.tlgrm;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class TlgrmCallbackContext extends TlgrmMessageContext {

  @NonNull
  @EqualsAndHashCode.Include
  private final String callbackId;

  public TlgrmCallbackContext(long chatId, long userId, long messageId, @NonNull String callbackId) {
    super(chatId, userId, messageId);
    this.callbackId = callbackId;
  }
}
