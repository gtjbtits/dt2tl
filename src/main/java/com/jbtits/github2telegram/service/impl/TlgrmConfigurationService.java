package com.jbtits.github2telegram.service.impl;

import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.cfg.mapping.TlgrmTribe;
import com.jbtits.github2telegram.service.ConfigurationService;
import lombok.NonNull;

import java.io.File;

public class TlgrmConfigurationService implements ConfigurationService<TlgrmChatContext> {

  @Override
  public @NonNull TribeConfiguration<TlgrmChatContext> build(@NonNull File file, @NonNull TlgrmChatContext context) {
    return null;
  }

  @Override
  public @NonNull TribeConfiguration<TlgrmChatContext> build(@NonNull TlgrmTribe request, @NonNull TlgrmChatContext context) {
    return null;
  }

  @Override
  public @NonNull String toString(@NonNull TribeConfiguration<TlgrmChatContext> configuration) {
    return null;
  }

  @Override
  public void apply(@NonNull TribeConfiguration<TlgrmChatContext> configuration) {

  }
}
