package com.jbtits.github2telegram.service.cfg;

import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.context.AbstractChatContext;
import com.jbtits.github2telegram.domain.dto.context.AbstractUserContext;
import lombok.NonNull;

public interface ConfigurationWizardService<C extends AbstractChatContext, U extends AbstractUserContext> {

  @NonNull
  TribeConfiguration<C,U> generateEmptyConfiguration(@NonNull final C context);

  boolean isTeamNameUnique(@NonNull final String teamName, @NonNull final TribeConfiguration<C, U> tribeConfiguration);

  void addTeam(@NonNull final String teamName, @NonNull final TribeConfiguration<C, U> tribeConfiguration);

  void removeTeam(@NonNull final String teamName, @NonNull final TribeConfiguration<C, U> tribeConfiguration);

  boolean changeTeam(@NonNull final String teamName, @NonNull final TribeConfiguration<C, U> tribeConfiguration,
                  @NonNull final U userContext);
}
