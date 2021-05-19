package com.jbtits.github2telegram.domain.event.tlgrm.cbq;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmCallbackContext;
import lombok.NonNull;

public class ConfigTeamJoinCallbackEvent extends AbstractTlgrmCallbackEvent<TlgrmCallbackContext> {

  public ConfigTeamJoinCallbackEvent(
      final @NonNull TlgrmCallbackContext context, final @NonNull Object[] actionArgs) {
    super(context, actionArgs);
  }
}
