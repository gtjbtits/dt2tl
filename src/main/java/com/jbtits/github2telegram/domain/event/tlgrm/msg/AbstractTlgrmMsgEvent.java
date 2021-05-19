package com.jbtits.github2telegram.domain.event.tlgrm.msg;

import com.jbtits.github2telegram.domain.dto.tlgrm.AbstractTlgrmContext;
import com.jbtits.github2telegram.domain.event.AbstractEvent;
import lombok.NonNull;

public abstract class AbstractTlgrmMsgEvent<C extends AbstractTlgrmContext> extends AbstractEvent<C> {

  protected AbstractTlgrmMsgEvent(@NonNull C context) {
    super(context);
  }
}
