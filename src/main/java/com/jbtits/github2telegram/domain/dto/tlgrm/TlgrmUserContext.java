package com.jbtits.github2telegram.domain.dto.tlgrm;

import com.jbtits.github2telegram.domain.dto.context.AbstractUserContext;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class TlgrmUserContext extends TlgrmChatContext implements AbstractUserContext {

  private final long userId;

  public TlgrmUserContext(long chatId, long userId) {
    super(chatId);
    this.userId = userId;
  }
}
