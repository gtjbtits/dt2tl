package com.jbtits.github2telegram.domain.dto.cfg;

import com.jbtits.github2telegram.domain.dto.context.AbstractUserContext;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode(callSuper = true)
public class FellowConfiguration<U extends AbstractUserContext> extends ConfigurationWithContext<U> {

  @NonNull
  private final String name;

  public FellowConfiguration(@NonNull final U context, @NonNull final String name) {
    super(context);
    this.name = name;
  }
}
