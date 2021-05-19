package com.jbtits.github2telegram.domain.event.tlgrm.msg.reply;

import com.jbtits.github2telegram.domain.dto.tlgrm.AbstractTlgrmContext;
import com.jbtits.github2telegram.domain.event.AbstractEvent;
import lombok.NonNull;

public abstract class AbstractTlgrmReplyEvent<C extends AbstractTlgrmContext> extends AbstractEvent<C> {

  protected AbstractTlgrmReplyEvent(@NonNull final C context) {
    super(context);
  }
}
