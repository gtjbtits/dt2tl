package com.jbtits.github2telegram.domain.event;

import com.jbtits.github2telegram.domain.dto.context.AbstractContext;
import lombok.Data;
import lombok.NonNull;

@Data
public abstract class AbstractEvent<C extends AbstractContext> {

  @NonNull
  private final C context;
}
