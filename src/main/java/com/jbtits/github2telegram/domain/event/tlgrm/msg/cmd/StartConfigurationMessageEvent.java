package com.jbtits.github2telegram.domain.event.tlgrm.msg.cmd;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmMessageContext;
import lombok.ToString;

@ToString(callSuper = true)
public class StartConfigurationMessageEvent extends AbstractTlgrmCmdEvent<TlgrmMessageContext> {

  public StartConfigurationMessageEvent(long chatId, long userId, long messageId) {
    super(new TlgrmMessageContext(chatId, userId, messageId));
  }
}
