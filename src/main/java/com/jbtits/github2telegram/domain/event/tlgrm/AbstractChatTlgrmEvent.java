package com.jbtits.github2telegram.domain.event.tlgrm;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.event.AbstractEvent;
import lombok.NonNull;
import lombok.ToString;

@ToString(callSuper = true)
public abstract class AbstractChatTlgrmEvent extends AbstractEvent<TlgrmChatContext> {

  AbstractChatTlgrmEvent(@NonNull TlgrmChatContext context) {
    super(context);
  }
}
