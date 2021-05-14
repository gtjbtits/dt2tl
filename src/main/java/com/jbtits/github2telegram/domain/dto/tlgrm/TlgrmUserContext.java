package com.jbtits.github2telegram.domain.dto.tlgrm;

import com.jbtits.github2telegram.domain.dto.common.AbstractContext;
import lombok.Data;
import lombok.NonNull;

@Data
public class TlgrmUserContext implements AbstractContext {

  @NonNull
  private final String id;

  @NonNull
  private final String username;
}
