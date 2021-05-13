package com.jbtits.github2telegram.domain.dto.tlgrm;

import com.jbtits.github2telegram.domain.dto.common.AbstractContext;
import lombok.Data;

@Data
public class TlgrmContext implements AbstractContext {
  private final long chatId;
}
