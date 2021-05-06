package com.jbtits.github2telegram.service;

import com.jbtits.github2telegram.domain.exception.FileDownloadException;
import lombok.NonNull;

import java.io.File;

public interface FileService {

  @NonNull File download(@NonNull String fileId) throws FileDownloadException;
}
