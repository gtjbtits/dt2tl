package com.jbtits.github2telegram.domain.event.tlgrm.msg;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import lombok.NonNull;

public class TlgrmAnyUsersMessageEvent extends AbstractTlgrmMsgEvent<TlgrmUserContext> {

  public TlgrmAnyUsersMessageEvent(@NonNull final TlgrmUserContext context) {
    super(context);
  }
}
