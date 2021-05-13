package com.jbtits.github2telegram.service.impl;

import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.entity.TribeRequest;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmContext;
import com.jbtits.github2telegram.service.ConfigurationService;
import lombok.NonNull;

import java.io.File;

public class TlgrmConfigurationService implements ConfigurationService<TlgrmContext> {

  @Override
  public @NonNull TribeConfiguration<TlgrmContext> build(@NonNull File file, @NonNull TlgrmContext context) {
    return null;
  }

  @Override
  public @NonNull TribeConfiguration<TlgrmContext> build(@NonNull TribeRequest request, @NonNull TlgrmContext context) {
    return null;
  }

  @Override
  public @NonNull String toString(@NonNull TribeConfiguration<TlgrmContext> configuration) {
    return null;
  }

  @Override
  public void apply(@NonNull TribeConfiguration<TlgrmContext> configuration) {

  }
}
