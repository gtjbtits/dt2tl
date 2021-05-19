package com.jbtits.github2telegram.listener.tlgrm.cbq;

import com.jbtits.github2telegram.component.tlgrm.TlgrmSender;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmCallbackContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.domain.event.tlgrm.cbq.ConfigCancelCallbackEvent;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmListenerException;
import com.jbtits.github2telegram.helpers.TlgrmMessageHelper;
import com.jbtits.github2telegram.helpers.TlgrmMetaHelper;
import com.jbtits.github2telegram.service.cfg.ConfigurationKeyValueService;
import com.jbtits.github2telegram.service.cfg.ConfigurationPersistenceService;
import com.jbtits.github2telegram.service.tlgrm.cfg.TlgrmConfigurationMessagesKeyValueService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import javax.transaction.Transactional;

@Slf4j
@Component
public class ConfigCancelCallbackListener
    extends AbstractTlgrmCallbackQueryListener<TlgrmCallbackContext, ConfigCancelCallbackEvent> {

  private final ConfigurationPersistenceService<TlgrmChatContext, TlgrmUserContext> configurationPersistenceService;
  private final ConfigurationKeyValueService<Long, TlgrmChatContext, TlgrmUserContext> configurationKeyValueService;
  private final TlgrmConfigurationMessagesKeyValueService tlgrmConfigurationMessagesKeyValueService;
  private final TlgrmSender tlgrmSender;

  protected ConfigCancelCallbackListener(final TlgrmMetaHelper tlgrmMetaHelper,
                                         final TlgrmMessageHelper tlgrmMessageHelper,
                                         final ConfigurationPersistenceService<TlgrmChatContext, TlgrmUserContext> configurationPersistenceService,
                                         final ConfigurationKeyValueService<Long, TlgrmChatContext, TlgrmUserContext> configurationKeyValueService,
                                         final TlgrmConfigurationMessagesKeyValueService tlgrmConfigurationMessagesKeyValueService,
                                         final TlgrmSender tlgrmSender) {
    super(tlgrmMetaHelper, tlgrmMessageHelper);
    this.configurationPersistenceService = configurationPersistenceService;
    this.configurationKeyValueService = configurationKeyValueService;
    this.tlgrmConfigurationMessagesKeyValueService = tlgrmConfigurationMessagesKeyValueService;
    this.tlgrmSender = tlgrmSender;
  }

  @Override
  protected @NonNull ConfigCancelCallbackEvent convertToEvent(final @NonNull CallbackQuery src)
      throws TlgrmListenerException {
    return new ConfigCancelCallbackEvent(tlgrmMetaHelper.extractCallbackContext(src));
  }

  @Override
  @Transactional
  protected void on(final @NonNull ConfigCancelCallbackEvent event) {
    final var context = event.getContext();
    this.configurationPersistenceService.activate(context);
    this.tlgrmConfigurationMessagesKeyValueService.removeAllConfigurationMessageIds();
    this.configurationKeyValueService.remove(context.getChatId());
    tlgrmMessageHelper.deleteMessage(context.getChatId(), context.getMessageId());
    tlgrmSender.answerCallbackQuery(event.getContext());
  }

  @Override
  protected Logger getLogger() {
    return log;
  }

  @Override
  protected TlgrmCallbackQueryAction getAction() {
    return TlgrmCallbackQueryAction.CONFIG_CANCEL;
  }
}
