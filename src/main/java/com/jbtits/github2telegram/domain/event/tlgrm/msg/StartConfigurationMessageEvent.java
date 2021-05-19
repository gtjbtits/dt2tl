package com.jbtits.github2telegram.domain.event.tlgrm.msg;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.event.AbstractEvent;
import lombok.ToString;

@ToString(callSuper = true)
public class StartConfigurationMessageEvent extends AbstractEvent<TlgrmChatContext> {

  public StartConfigurationMessageEvent(long chatId) {
    super(new TlgrmChatContext(chatId));
  }
}
