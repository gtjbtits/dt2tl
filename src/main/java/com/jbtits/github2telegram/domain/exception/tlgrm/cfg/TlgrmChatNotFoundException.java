package com.jbtits.github2telegram.domain.exception.tlgrm.cfg;

import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmRuntimeException;

public class TlgrmChatNotFoundException extends TlgrmRuntimeException {

  public TlgrmChatNotFoundException(final String message) {
    super(message);
  }
}
