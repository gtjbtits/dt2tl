package com.jbtits.github2telegram.domain.exception.tlgrm.cfg;

import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmRuntimeException;

public class TlgrmUserNotFoundException extends TlgrmRuntimeException {

  public TlgrmUserNotFoundException(final String message) {
    super(message);
  }
}
