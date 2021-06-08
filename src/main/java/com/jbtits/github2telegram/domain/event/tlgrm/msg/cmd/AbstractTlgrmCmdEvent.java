package com.jbtits.github2telegram.domain.event.tlgrm.msg.cmd;

import com.jbtits.github2telegram.domain.dto.tlgrm.AbstractTlgrmContext;
import com.jbtits.github2telegram.domain.event.AbstractEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractTlgrmCmdEvent<C extends AbstractTlgrmContext> extends AbstractEvent<C> {

  protected AbstractTlgrmCmdEvent(@NonNull C context) {
    super(context);
  }
}
