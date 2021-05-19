package com.jbtits.github2telegram.listener.tlgrm.cbq;

import com.jbtits.github2telegram.component.tlgrm.TlgrmSender;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmCallbackContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.domain.event.tlgrm.cbq.ConfigSaveCallbackEvent;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmListenerException;
import com.jbtits.github2telegram.helpers.TlgrmMessageHelper;
import com.jbtits.github2telegram.helpers.TlgrmMetaHelper;
import com.jbtits.github2telegram.service.cfg.ConfigurationKeyValueService;
import com.jbtits.github2telegram.service.cfg.ConfigurationPersistenceService;
import com.jbtits.github2telegram.service.tlgrm.cfg.TlgrmConfigurationMessagesKeyValueService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import javax.transaction.Transactional;

@Slf4j
@Service
public class ConfigSaveCallbackListener extends AbstractTlgrmCallbackQueryListener<TlgrmCallbackContext, ConfigSaveCallbackEvent> {

  private final ConfigurationKeyValueService<Long, TlgrmChatContext, TlgrmUserContext> configurationKeyValueService;
  private final ConfigurationPersistenceService<TlgrmChatContext, TlgrmUserContext> configurationPersistenceService;
  private final TlgrmConfigurationMessagesKeyValueService tlgrmConfigurationMessagesKeyValueService;
  private final TlgrmSender tlgrmSender;

  protected ConfigSaveCallbackListener(final TlgrmMetaHelper tlgrmMetaHelper,
                                       final TlgrmMessageHelper tlgrmMessageHelper,
                                       final ConfigurationKeyValueService<Long, TlgrmChatContext, TlgrmUserContext> configurationKeyValueService,
                                       final ConfigurationPersistenceService<TlgrmChatContext, TlgrmUserContext> configurationPersistenceService,
                                       final TlgrmConfigurationMessagesKeyValueService tlgrmConfigurationMessagesKeyValueService,
                                       final TlgrmSender tlgrmSender) {
    super(tlgrmMetaHelper, tlgrmMessageHelper);
    this.configurationKeyValueService = configurationKeyValueService;
    this.configurationPersistenceService = configurationPersistenceService;
    this.tlgrmConfigurationMessagesKeyValueService = tlgrmConfigurationMessagesKeyValueService;
    this.tlgrmSender = tlgrmSender;
  }

  @Override
  protected @NonNull ConfigSaveCallbackEvent convertToEvent(final @NonNull CallbackQuery src)
      throws TlgrmListenerException {
    return new ConfigSaveCallbackEvent(tlgrmMetaHelper.extractCallbackContext(src));
  }

  @Override
  @Transactional
  protected void on(final @NonNull ConfigSaveCallbackEvent event) {
    final var context = event.getContext();
    this.configurationKeyValueService.get(context.getChatId()).ifPresent(tribeConfiguration -> {
      this.configurationPersistenceService.save(tribeConfiguration);
      this.configurationPersistenceService.activate(context);
      this.tlgrmConfigurationMessagesKeyValueService.removeAllConfigurationMessageIds();
      this.configurationKeyValueService.remove(context.getChatId());
      this.tlgrmMessageHelper.finalizeConfigurationMessage(context, tribeConfiguration);
    });
    this.tlgrmSender.answerCallbackQuery(context);
  }

  @Override
  protected Logger getLogger() {
    return log;
  }

  @Override
  protected TlgrmCallbackQueryAction getAction() {
    return TlgrmCallbackQueryAction.CONFIG_SAVE;
  }
}
