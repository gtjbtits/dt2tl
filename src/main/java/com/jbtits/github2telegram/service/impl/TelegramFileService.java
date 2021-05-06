package com.jbtits.github2telegram.service.impl;

import com.jbtits.github2telegram.component.Dt2TlBot;
import com.jbtits.github2telegram.domain.exception.FileDownloadException;
import com.jbtits.github2telegram.service.FileService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

@Service
@RequiredArgsConstructor
public class TelegramFileService implements FileService {

  private final Dt2TlBot tlBot;

  @Override
  public @NonNull File download(@NonNull String fileId) throws FileDownloadException {
    try {
      final GetFile getFile = new GetFile();
      getFile.setFileId(fileId);
      final String filePath = tlBot.execute(getFile).getFilePath();
      return tlBot.downloadFile(filePath);
    } catch (TelegramApiException e) {
      throw new FileDownloadException(e);
    }
  }
}
