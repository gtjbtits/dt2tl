package com.jbtits.github2telegram.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Configuration
public class I18nConfig {

  public static final Locale LOCALE_RU = new Locale("ru", "RU");

  @Bean
  ResourceBundleMessageSource messageSource() {
    final var messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
    messageSource.setDefaultLocale(LOCALE_RU);
    return messageSource;
  }
}
