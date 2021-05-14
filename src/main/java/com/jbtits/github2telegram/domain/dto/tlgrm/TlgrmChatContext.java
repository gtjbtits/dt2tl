package com.jbtits.github2telegram.domain.dto.tlgrm;

import com.jbtits.github2telegram.domain.dto.common.AbstractContext;
import lombok.Data;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

@Data
public class TlgrmChatContext implements AbstractContext {

  @NonNull
  private final long id;

  private final Set<TlgrmUserContext> userContextSet = new HashSet<>();
}
