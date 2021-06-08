package com.jbtits.github2telegram.domain.exception.tlgrm;

public class TlgrmMetadataObtainingException extends RuntimeException {

  public TlgrmMetadataObtainingException(String message) {
    super(message);
  }

  public TlgrmMetadataObtainingException(Throwable cause) {
    super(cause);
  }
}
