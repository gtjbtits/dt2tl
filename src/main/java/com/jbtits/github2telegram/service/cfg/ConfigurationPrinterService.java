package com.jbtits.github2telegram.service.cfg;

import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.context.AbstractChatContext;
import com.jbtits.github2telegram.domain.dto.context.AbstractUserContext;
import lombok.NonNull;

public interface ConfigurationPrinterService<C extends AbstractChatContext, U extends AbstractUserContext> {

  @NonNull
  String printTeams(@NonNull final TribeConfiguration<C,U> tribeConfiguration);

  @NonNull
  String printTeamsWithFellows(@NonNull final TribeConfiguration<C,U> tribeConfiguration);
}
