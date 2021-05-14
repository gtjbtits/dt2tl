package com.jbtits.github2telegram.util;

import com.jbtits.github2telegram.domain.exception.FileReadException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
public class YamlUtils {

  private YamlUtils() {

  }

  @NonNull
  public static <T> T parse(@NonNull File file, @NonNull Class<T> clazz) {
    final var constructor = new Constructor(clazz);
    final var yaml = new Yaml(constructor);
    try (final var fis = new FileInputStream(file)) {
      return yaml.load(fis);
    } catch (IOException e) {
      throw new FileReadException(e);
    }
  }
}
