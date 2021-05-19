package com.jbtits.github2telegram.domain.dto.tlgrm;

import com.jbtits.github2telegram.domain.dto.context.AbstractUserContext;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class TlgrmUserContext extends TlgrmChatContext implements AbstractUserContext {

  @ToString.Include
  @EqualsAndHashCode.Include
  private final long userId;

  public TlgrmUserContext(long chatId, long userId) {
    super(chatId);
    this.userId = userId;
  }

  public TlgrmUserContext(@NonNull final TlgrmChatContext chatContext, long userId) {
    this(chatContext.getChatId(), userId);
  }
}
