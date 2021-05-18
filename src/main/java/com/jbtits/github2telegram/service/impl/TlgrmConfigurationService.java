package com.jbtits.github2telegram.service.impl;

import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.cfg.yml.TlgrmTribe;
import com.jbtits.github2telegram.service.ConfigurationService;
import lombok.NonNull;

import java.io.File;

public class TlgrmConfigurationService implements ConfigurationService<TlgrmChatContext, TlgrmUserContext> {

  @Override
  public @NonNull TribeConfiguration<TlgrmChatContext, TlgrmUserContext> build(
      @NonNull File file,
      @NonNull TlgrmChatContext context) {
    return null;
  }

  @Override
  public @NonNull TribeConfiguration<TlgrmChatContext, TlgrmUserContext> build(
      @NonNull TlgrmTribe request,
      @NonNull TlgrmChatContext context) {
    return null;
  }

  @Override
  public @NonNull String toString(@NonNull TribeConfiguration<TlgrmChatContext, TlgrmUserContext> configuration) {
    return null;
  }

  @Override
  public void apply(@NonNull TribeConfiguration<TlgrmChatContext, TlgrmUserContext> configuration) {

  }
}
