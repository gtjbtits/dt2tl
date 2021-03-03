package com.jbtits.github2telegram.listener;

import com.jbtits.github2telegram.domain.dto.CodeReviewAnnounceDto;
import com.jbtits.github2telegram.domain.event.NewUrlMessageEvent;
import com.jbtits.github2telegram.persistence.entity.Developer;
import com.jbtits.github2telegram.persistence.service.DeveloperService;
import com.jbtits.github2telegram.service.AnnounceService;
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
  public void handleCodeReviewUrl(NewUrlMessageEvent msg) {
    final String url = msg.getUrl();
    if (!url.contains(this.prefix)) {
      log.debug("Link {} ignored, because doesn't contains CR url prefix {}", url, this.prefix);
      return;
    }
    final Set<Developer> reviewers = this.developerService.findReviewers(msg.getUsername());
    if (reviewers.isEmpty()) {
      log.warn("Can't announce review to PR {}, because reviewers set is empty", url);
    }
    final String[] to = reviewers.stream()
        .map(Developer::getUsername)
        .map(this::convertToMention)
        .toArray(String[]::new);
    final CodeReviewAnnounceDto announceDto = new CodeReviewAnnounceDto(
        this.convertToMention(msg.getUsername()),
        to,
        url,
        msg.getChatId());
    this.announceService.makeAnnounceForReviewers(announceDto);
  }

  private String convertToMention(String username) {
    return "@" + username;
  }
}
