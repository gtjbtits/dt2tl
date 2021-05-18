package com.jbtits.github2telegram.service.impl;

import com.jbtits.github2telegram.component.tlgrm.Dt2TlBot;
import com.jbtits.github2telegram.domain.dto.announce.CodeReviewAnnounce;
import com.jbtits.github2telegram.domain.dto.announce.MentionAnnounce;
import com.jbtits.github2telegram.domain.dto.announce.TextAnnounce;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.service.AnnounceService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;


@Service
@RequiredArgsConstructor
public class TelegramAnnounceService implements AnnounceService {

  private static final Random RANDOM = new Random();

  private static final String[] ANNOUNCE_TEXTS = {
      "Review fewer than 400 lines of code at a time",
      "Take your time. Inspection rates should under 500 LOC per hour",
      "Do not review for more than 60 minutes at a time",
      "Set goals and capture metrics",
      "Authors should annotate source code before the review",
      "Use checklists",
      "Establish a process for fixing defects found",
      "Foster a positive code review culture",
      "Embrace the subconscious implications of peer review",
      "Practice lightweight code reviews"
  };

  private final Dt2TlBot bot;

  public void makeAnnounceForReviewers(@NonNull CodeReviewAnnounce announceDto) {
    final String announceText = ANNOUNCE_TEXTS[RANDOM.nextInt(ANNOUNCE_TEXTS.length)];
    final String reviewers = String.join(", ", announceDto.getTo());
    final String text = String.format("%s%n%s%n%nDeveloper: %s%nReviewers: %s",
        announceText,
        announceDto.getUrl(),
        announceDto.getFrom(),
        reviewers);
    bot.sendMessage(text, new TlgrmChatContext(announceDto.getChatId()));
  }

  public void mention(@NonNull MentionAnnounce mentionAnnounce) {
    final String text = String.join(" ", mentionAnnounce.getTo());
    bot.sendMessage(text, new TlgrmChatContext(mentionAnnounce.getChatId()));
  }

  public void sendText(@NonNull TextAnnounce textAnnounce) {
    bot.sendMessage(textAnnounce.getText(), new TlgrmChatContext(textAnnounce.getChatId()));
  }
}
