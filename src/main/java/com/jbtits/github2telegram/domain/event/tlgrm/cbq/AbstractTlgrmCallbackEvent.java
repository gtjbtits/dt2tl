package com.jbtits.github2telegram.domain.event.tlgrm.cbq;

import com.jbtits.github2telegram.domain.dto.tlgrm.AbstractTlgrmContext;
import com.jbtits.github2telegram.domain.event.AbstractEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public abstract class AbstractTlgrmCallbackEvent<C extends AbstractTlgrmContext> extends AbstractEvent<C> {

  @NonNull
  @EqualsAndHashCode.Include
  private final Object[] actionArgs;

  protected AbstractTlgrmCallbackEvent(@NonNull final C context, @NonNull final Object[] actionArgs) {
    super(context);
    this.actionArgs = actionArgs;
  }

  protected AbstractTlgrmCallbackEvent(@NonNull final C context) {
    this(context, new Object[0]);
  }
}
