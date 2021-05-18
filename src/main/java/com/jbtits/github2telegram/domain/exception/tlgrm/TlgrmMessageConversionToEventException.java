package com.jbtits.github2telegram.domain.exception.tlgrm;

public class TlgrmMessageConversionToEventException extends Exception {

  public TlgrmMessageConversionToEventException(String s) {
    super(s);
  }

  public TlgrmMessageConversionToEventException(Throwable cause) {
    super(cause);
  }
}
