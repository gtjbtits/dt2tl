package com.jbtits.github2telegram.util;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class CollectionUtils {

  private CollectionUtils() {

  }

  @NonNull
  public static <T> List<T> combineOneAfterOne(@NonNull final Collection<T> odd, @NonNull final Collection<T> even) {
    final var oddQueue = new LinkedList<T>(odd);
    final var evenQueue = new LinkedList<>(even);
    final var result = new ArrayList<T>();
    while (!oddQueue.isEmpty() || !evenQueue.isEmpty()) {
      Optional.ofNullable(oddQueue.poll()).ifPresent(result::add);
      Optional.ofNullable(evenQueue.poll()).ifPresent(result::add);
    }
    return result;
  }
}
