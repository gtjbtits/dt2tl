package com.jbtits.github2telegram.domain.event.tlgrm.cbq;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmCallbackContext;
import com.jbtits.github2telegram.domain.event.AbstractEvent;
import lombok.NonNull;

public class ConfigJoinCallbackEvent extends AbstractEvent<TlgrmCallbackContext> {

  public ConfigJoinCallbackEvent(@NonNull TlgrmCallbackContext context) {
    super(context);
  }
}
