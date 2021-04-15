package com.jbtits.github2telegram.listener;

import com.jbtits.github2telegram.domain.dto.announce.CodeReviewAnnounce;
import com.jbtits.github2telegram.domain.event.NewUrlMessageEvent;
import com.jbtits.github2telegram.persistence.entity.Fellow;
import com.jbtits.github2telegram.persistence.service.DeveloperService;
import com.jbtits.github2telegram.service.AnnounceService;
import com.jbtits.github2telegram.util.TelegramMessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CodeReviewMsgListener {

  private final DeveloperService developerService;
  private final AnnounceService announceService;

  @Value("${pr.prefix}")
  private String prefix;

  @EventListener
  public void handleCodeReviewUrl(NewUrlMessageEvent event) {
    log.debug("Handling newUrlMessage event '{}'. Current PR prefix is '{}'", event, this.prefix);
    final String url = event.getUrl();
    if (!url.contains(this.prefix)) {
      log.debug("Link '{}' ignored, because doesn't contains CR url prefix '{}'", url, this.prefix);
      return;
    }
    final Set<Fellow> reviewers = this.developerService.findReviewers(event.getUsername());
    if (reviewers.isEmpty()) {
      log.warn("Can't announce review to PR '{}', because reviewers set is empty", url);
    }
    final CodeReviewAnnounce announceDto = new CodeReviewAnnounce(
        TelegramMessageUtils.toMention(event.getUsername()),
        TelegramMessageUtils.toMentions(reviewers, fellow -> {throw new RuntimeException("Telegram @username needed here");}),
        url,
        event.getChatId());
    this.announceService.makeAnnounceForReviewers(announceDto);
  }
}
