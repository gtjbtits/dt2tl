package com.jbtits.github2telegram.helpers;

import com.jbtits.github2telegram.configuration.properties.BotApiProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;

@Component
@RequiredArgsConstructor
public class TlgrmHelper {

  private static final String TELEGRAM_AT_PREFIX = "@";

  private final DefaultAbsSender tlgrmBot;
  private final BotApiProperties botApiProperties;

  public boolean textContainsCommand(@NonNull String text, @NonNull String command) {
    return text.equals(command) || text.equals(command + TELEGRAM_AT_PREFIX + botApiProperties.getUsername());
  }
}
