package com.jbtits.github2telegram.util;

import com.jbtits.github2telegram.persistence.entity.BaseIdEntity;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RandomUtils {

  private static final Random RANDOM = new Random();

  private RandomUtils() {

  }

  public static <T extends BaseIdEntity> Optional<T> getRandom(@NonNull List<T> entities) {
    if (entities.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(entities.get(RandomUtils.getRandomId(entities)));
  }

  public static <T extends BaseIdEntity> List<T> getRandoms(@NonNull List<T> entities, int max) {
    final List<T> dest = new ArrayList<>();
    RandomUtils.getRandoms(entities, dest, max);
    return dest;
  }

  private static <T extends BaseIdEntity> void getRandoms(@NonNull List<T> src, @NonNull List<T> dst, int toGo) {
    if (toGo < 0) {
      throw new IllegalArgumentException("toGo must be greater than or equals zero");
    } else if (toGo > src.size()) {
      dst.clear();
      dst.addAll(src);
      return;
    } else if (toGo == 0) {
      return;
    }
    final int randomId = RandomUtils.getRandomId(src);
    final var randomEntity = src.remove(randomId);
    dst.add(randomEntity);
    RandomUtils.getRandoms(src, dst, toGo - 1);
  }

  private static <T extends BaseIdEntity> int getRandomId(@NonNull List<T> entities) {
    return RANDOM.nextInt(entities.size());
  }
}