package com.jbtits.github2telegram.helpers;

import com.jbtits.github2telegram.configuration.I18nConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageHelper {

  private final MessageSource messageSource;

  @NonNull
  public String getMsg(@NonNull final String code, final Object ... args) {
    return messageSource.getMessage(code, args, I18nConfig.LOCALE_RU);
  }
}
