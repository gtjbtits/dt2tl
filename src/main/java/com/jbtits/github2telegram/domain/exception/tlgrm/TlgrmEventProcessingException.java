package com.jbtits.github2telegram.domain.exception.tlgrm;

public class TlgrmEventProcessingException extends Exception {

  public TlgrmEventProcessingException(String s) {
    super(s);
  }

  public TlgrmEventProcessingException(Throwable cause) {
    super(cause);
  }
}
