package com.jbtits.github2telegram.listener.tlgrm.cbq;

import com.jbtits.github2telegram.component.tlgrm.TlgrmSender;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmCallbackContext;
import com.jbtits.github2telegram.domain.event.tlgrm.cbq.ConfigJoinCallbackEvent;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmEventProcessingException;
import com.jbtits.github2telegram.helpers.TlgrmHelper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static com.jbtits.github2telegram.listener.tlgrm.cbq.TlgrmCallbackQueryData.CONFIG_JOIN;

@Slf4j
@Component
public class ConfigJoinCallbackListener
    extends AbstractTlgrmCallbackQueryListener<TlgrmCallbackContext, ConfigJoinCallbackEvent> {

  private final TlgrmSender tlgrmSender;

  ConfigJoinCallbackListener(TlgrmHelper tlgrmHelper, TlgrmSender tlgrmSender) {
    super(tlgrmHelper);
    this.tlgrmSender = tlgrmSender;
  }

  @Override
  protected String getData() {
    return CONFIG_JOIN;
  }

  @Override
  protected @NonNull ConfigJoinCallbackEvent convertToEvent(@NonNull CallbackQuery callbackQuery)
      throws TlgrmEventProcessingException {
    return new ConfigJoinCallbackEvent(tlgrmHelper.extractCallbackContext(callbackQuery));
  }

  @Override
  protected void on(@NonNull ConfigJoinCallbackEvent event) {
    tlgrmSender.answerCallbackQuery(event.getContext());
  }

  @Override
  protected Logger getLogger() {
    return log;
  }
}
