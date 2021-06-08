package com.jbtits.github2telegram.domain.event.tlgrm.msg;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmMessageContext;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class TlgrmPrMessageEvent extends AbstractTlgrmMsgEvent<TlgrmMessageContext> {

  @NonNull
  @EqualsAndHashCode.Include
  private final String prLink;

  public TlgrmPrMessageEvent(@NonNull final TlgrmMessageContext context, @NonNull final String prLink) {
    super(context);
    this.prLink = prLink;
  }
}
