package com.jbtits.github2telegram.domain.exception.tlgrm.cfg;

import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmRuntimeException;

public class TlgrmTribeNotActiveException extends TlgrmRuntimeException {
  public TlgrmTribeNotActiveException(final String message) {
    super(message);
  }
}
