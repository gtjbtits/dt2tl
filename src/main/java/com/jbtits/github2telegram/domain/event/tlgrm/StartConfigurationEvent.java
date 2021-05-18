package com.jbtits.github2telegram.domain.event.tlgrm;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import lombok.ToString;

@ToString(callSuper = true)
public class StartConfigurationEvent extends AbstractChatTlgrmEvent {

  public StartConfigurationEvent(long chatId) {
    super(new TlgrmChatContext(chatId));
  }
}
