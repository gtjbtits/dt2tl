package com.jbtits.github2telegram.domain.dto.tlgrm;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class TlgrmMessageContext extends TlgrmUserContext {

  @ToString.Include
  @EqualsAndHashCode.Include
  private final long messageId;

  public TlgrmMessageContext(final long chatId, final long userId, long messageId) {
    super(chatId, userId);
    this.messageId = messageId;
  }
}
