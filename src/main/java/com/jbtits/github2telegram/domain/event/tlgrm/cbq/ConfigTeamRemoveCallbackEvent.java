package com.jbtits.github2telegram.domain.event.tlgrm.cbq;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmCallbackContext;
import lombok.NonNull;

public class ConfigTeamRemoveCallbackEvent extends AbstractTlgrmCallbackEvent<TlgrmCallbackContext> {

  public ConfigTeamRemoveCallbackEvent(
      @NonNull final TlgrmCallbackContext context,
      @NonNull final Object[] actionArgs) {
    super(context, actionArgs);
  }
}
