package com.jbtits.github2telegram.domain.event.tlgrm;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.domain.event.AbstractEvent;
import lombok.NonNull;

public abstract class AbstractUserTlgrmEvent extends AbstractEvent<TlgrmUserContext> {

  AbstractUserTlgrmEvent(@NonNull TlgrmUserContext context) {
    super(context);
  }
}
