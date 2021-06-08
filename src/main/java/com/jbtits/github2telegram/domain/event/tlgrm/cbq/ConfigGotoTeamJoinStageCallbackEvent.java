package com.jbtits.github2telegram.domain.event.tlgrm.cbq;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmCallbackContext;
import lombok.NonNull;

public class ConfigGotoTeamJoinStageCallbackEvent extends AbstractTlgrmCallbackEvent<TlgrmCallbackContext> {

  public ConfigGotoTeamJoinStageCallbackEvent(@NonNull TlgrmCallbackContext context) {
    super(context);
  }
}
