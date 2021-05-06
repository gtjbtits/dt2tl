package com.jbtits.github2telegram.listener;

import com.jbtits.github2telegram.domain.dto.announce.TextAnnounce;
import com.jbtits.github2telegram.domain.event.NewConfigEvent;
import com.jbtits.github2telegram.domain.exception.FileDownloadException;
import com.jbtits.github2telegram.service.AnnounceService;
import com.jbtits.github2telegram.service.FileService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewConfigEventListener {

  private final FileService fileService;
  private final AnnounceService announceService;

  @EventListener
  public void handleNewConfigEvent(@NonNull NewConfigEvent event) {
    final String fileId = event.getFileId();
    final long chatId = event.getChatId();
    final File cfgFile;
    try {
      cfgFile = this.fileService.download(fileId);
    } catch (FileDownloadException e) {
      this.announceService.sendText(new TextAnnounce(chatId,
          "Error while configuration downloading. Please see logs for details"));
      log.error("Error while configuration downloading {}", fileId, e);
      return;
    }
    try (final FileInputStream fis = new FileInputStream(cfgFile)) {
      final String text = new String(fis.readAllBytes());
      this.announceService.sendText(new TextAnnounce(chatId, text));
    } catch (FileNotFoundException e) {
      this.announceService.sendText(new TextAnnounce(chatId,
          "Error while configuration reading. Please see logs for details"));
      log.error("Can't find downloaded configuration file {}", fileId, e);
    } catch (IOException e) {
      this.announceService.sendText(new TextAnnounce(chatId,
          "Error while configuration reading. Please see logs for details"));
      log.error("Error while reading downloaded configuration {}", fileId, e);
    }
  }
}
