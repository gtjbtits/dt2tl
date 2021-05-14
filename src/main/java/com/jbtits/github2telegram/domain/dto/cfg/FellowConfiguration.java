package com.jbtits.github2telegram.domain.dto.cfg;

import com.jbtits.github2telegram.domain.dto.common.AbstractContext;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode(callSuper = true)
public class FellowConfiguration<C extends AbstractContext> extends ConfigurationPart<C> {

  @NonNull
  private final String name;

  public FellowConfiguration(@NonNull final C context, @NonNull final String name) {
    super(context);
    this.name = name;
  }
}
