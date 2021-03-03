package com.jbtits.github2telegram.service;

import com.jbtits.github2telegram.component.G2TBot;
import com.jbtits.github2telegram.domain.dto.CodeReviewAnnounceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;


@Service
@RequiredArgsConstructor
public class AnnounceService {

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

  private final G2TBot bot;

  public void makeAnnounceForReviewers(CodeReviewAnnounceDto announceDto) {
    final String announceText = ANNOUNCE_TEXTS[RANDOM.nextInt(ANNOUNCE_TEXTS.length)];
    final String reviewers = String.join(", ", announceDto.getTo());
    final String text = String.format("%s%n%s%n%nDeveloper: %s%nReviewers: %s",
        announceText,
        announceDto.getUrl(),
        announceDto.getFrom(),
        reviewers);
    bot.sendMessage(text, announceDto.getChatId());
  }
}
