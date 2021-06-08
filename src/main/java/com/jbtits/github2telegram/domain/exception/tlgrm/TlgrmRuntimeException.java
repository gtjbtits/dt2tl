package com.jbtits.github2telegram.domain.exception.tlgrm;

public abstract class TlgrmRuntimeException extends RuntimeException {

  protected TlgrmRuntimeException(final String message) {
    super(message);
  }

  protected TlgrmRuntimeException(final String message, final Throwable cause) {
    super(message, cause);
  }

  protected TlgrmRuntimeException(final Throwable cause) {
    super(cause);
  }
}
