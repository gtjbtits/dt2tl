package com.jbtits.github2telegram.util;

import com.jbtits.github2telegram.persistence.entity.BaseIdEntity;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RandomUtil {

  private static final Random RANDOM = new Random();

  private RandomUtil() {

  }

  public static <T extends BaseIdEntity> Optional<T> getRandom(@NonNull List<T> entities) {
    if (entities.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(entities.get(RandomUtil.getRandomId(entities)));
  }

  public static <T extends BaseIdEntity> List<T> getRandoms(@NonNull List<T> entities, int amount) {
    final List<T> dest = new ArrayList<>();
    RandomUtil.getRandoms(entities, dest, amount);
    return dest;
  }

  private static <T extends BaseIdEntity> void getRandoms(@NonNull List<T> source, @NonNull List<T> dest, int toGo) {
    if (toGo > source.size()) {
      throw new IllegalArgumentException("toGo must be less or equal to source size");
    } else if (toGo < 0) {
      throw new IllegalArgumentException("toGo must be greater than or equals zero");
    }
    if (toGo == 0) {
      return;
    }
    final int randomId = RandomUtil.getRandomId(source);
    final T randomEntity = source.remove(randomId);
    dest.add(randomEntity);
    RandomUtil.getRandoms(source, dest, toGo - 1);
  }

  private static <T extends BaseIdEntity> int getRandomId(@NonNull List<T> entities) {
    return RANDOM.nextInt(entities.size());
  }
}
