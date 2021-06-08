package com.jbtits.github2telegram.domain.event.tlgrm.cbq;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmCallbackContext;
import lombok.NonNull;

public class ConfigBackCallbackEvent extends AbstractTlgrmCallbackEvent<TlgrmCallbackContext> {

  public ConfigBackCallbackEvent(
      final @NonNull TlgrmCallbackContext context) {
    super(context);
  }
}
