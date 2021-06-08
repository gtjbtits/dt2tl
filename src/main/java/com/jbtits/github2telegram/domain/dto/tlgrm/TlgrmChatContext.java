package com.jbtits.github2telegram.domain.dto.tlgrm;

import com.jbtits.github2telegram.domain.dto.context.AbstractChatContext;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TlgrmChatContext implements AbstractChatContext, AbstractTlgrmContext {

  @EqualsAndHashCode.Include
  private final long chatId;
}
