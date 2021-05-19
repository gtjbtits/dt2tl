package com.jbtits.github2telegram.listener.tlgrm.msg;

import com.jbtits.github2telegram.component.tlgrm.TlgrmSender;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.event.tlgrm.msg.StartConfigurationMessageEvent;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmEventProcessingException;
import com.jbtits.github2telegram.helpers.TlgrmHelper;
import com.jbtits.github2telegram.listener.tlgrm.AbstractTlgrmListener;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
public class StartConfigurationEventListener
    extends AbstractTlgrmMessageListener<TlgrmChatContext, StartConfigurationMessageEvent> {

  private static final String START_TELEGRAM_CMD = "/start";

  private final TlgrmSender tlgrmSender;

  StartConfigurationEventListener(TlgrmHelper tlgrmHelper, TlgrmSender tlgrmSender) {
    super(tlgrmHelper);
    this.tlgrmSender = tlgrmSender;
  }

  @Override
  protected @NonNull StartConfigurationMessageEvent convertToEvent(@NonNull Message message)
      throws TlgrmEventProcessingException {
    final var chat = message.getChat();
    AbstractTlgrmMessageListener.hasType(message.getEntities(),
        AbstractTlgrmListener.MESSAGE_ENTITY_CMD_TYPE);
    final long chatId = chat.getId();
    this.textContainsCommand(message.getText(), START_TELEGRAM_CMD);
    return new StartConfigurationMessageEvent(chatId);
  }

  @Override
  protected void on(@NonNull StartConfigurationMessageEvent event) {
    tlgrmSender.sendMessage(tlgrmHelper.constructCfgMessage(event.getContext()));
  }

  @Override
  protected Logger getLogger() {
    return log;
  }
}
