package com.jbtits.github2telegram.domain.dto.tlgrm;

import com.jbtits.github2telegram.domain.dto.context.AbstractChatContext;
import lombok.Data;

@Data
public class TlgrmChatContext implements AbstractChatContext, AbstractTlgrmContext {

  private final long chatId;
}
