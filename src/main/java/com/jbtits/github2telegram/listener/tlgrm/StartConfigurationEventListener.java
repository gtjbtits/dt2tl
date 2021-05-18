package com.jbtits.github2telegram.listener.tlgrm;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.event.tlgrm.StartConfigurationEvent;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmMessageConversionToEventException;
import com.jbtits.github2telegram.helpers.TlgrmHelper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
public class StartConfigurationEventListener
    extends AbstractTlgrmMessageListener<TlgrmChatContext, StartConfigurationEvent> {

  private static final String START_TELEGRAM_CMD = "/start";

  StartConfigurationEventListener(TlgrmHelper tlgrmHelper) {
    super(tlgrmHelper);
  }

  @Override
  protected @NonNull StartConfigurationEvent convertToEvent(@NonNull Message message)
      throws TlgrmMessageConversionToEventException {
    final var chat = message.getChat();
    AbstractTlgrmMessageListener.hasType(message.getEntities(),
        AbstractTlgrmMessageListener.MESSAGE_ENTITY_CMD_TYPE);
    final long chatId = chat.getId();
    this.textContainsCommand(message.getText(), START_TELEGRAM_CMD);
    return new StartConfigurationEvent(chatId);
  }

  @Override
  protected void on(@NonNull StartConfigurationEvent event) {
    log.info("New event {}", event);
  }

  @Override
  protected Logger getLogger() {
    return log;
  }
}
