package com.jbtits.github2telegram.domain.exception.tlgrm.cfg;

import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmRuntimeException;

public class TlgrmFellowNotFoundException extends TlgrmRuntimeException {

  public TlgrmFellowNotFoundException(final String message) {
    super(message);
  }
}
