package com.jbtits.github2telegram.listener;

import com.jbtits.github2telegram.domain.dto.announce.MentionAnnounce;
import com.jbtits.github2telegram.domain.event.MentionAllEvent;
import com.jbtits.github2telegram.persistence.entity.Fellow;
import com.jbtits.github2telegram.persistence.service.DeveloperService;
import com.jbtits.github2telegram.service.AnnounceService;
import com.jbtits.github2telegram.util.TelegramMessageUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MentionAllCmdListener {

  private final AnnounceService announceService;
  private final DeveloperService developerService;

  @EventListener
  public void handleMentionAllEvent(@NonNull MentionAllEvent event) {
    final List<Fellow> fellows = this.developerService.findAll();
    final MentionAnnounce mentionAnnounce = new MentionAnnounce(
        event.getChatId(),
        TelegramMessageUtils.toMentions(fellows, fellow -> {throw new RuntimeException("Telegram @username needed here");})
    );
    this.announceService.mention(mentionAnnounce);
  }
}
