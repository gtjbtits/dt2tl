package com.jbtits.github2telegram.domain.exception;

import java.io.IOException;

public class FileDownloadException extends IOException {
  public FileDownloadException(Throwable cause) {
    super(cause);
  }
}
