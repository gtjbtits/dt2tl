package com.jbtits.github2telegram.util;

import lombok.NonNull;

import java.util.Collection;
import java.util.function.Function;

public class TelegramMessageUtils {

  private TelegramMessageUtils() {

  }

  public static String toMention(@NonNull String username) {
    return "@" + username;
  }

  public static <T> String[] toMentions(@NonNull Collection<T> objs, Function<T, String> getter) {
    return objs.stream()
        .map(getter)
        .toArray(String[]::new);
  }
}
