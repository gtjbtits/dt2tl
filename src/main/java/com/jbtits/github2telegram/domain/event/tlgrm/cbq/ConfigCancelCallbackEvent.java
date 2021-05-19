package com.jbtits.github2telegram.domain.event.tlgrm.cbq;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmCallbackContext;
import lombok.NonNull;

public class ConfigCancelCallbackEvent extends AbstractTlgrmCallbackEvent<TlgrmCallbackContext> {

  public ConfigCancelCallbackEvent(
      @NonNull final TlgrmCallbackContext context) {
    super(context);
  }
}
