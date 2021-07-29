package com.jbtits.github2telegram.listener.tlgrm.msg.cmd;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmBotCmd;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmMessageContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.domain.event.tlgrm.msg.cmd.StartConfigurationMessageEvent;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmListenerException;
import com.jbtits.github2telegram.helpers.TlgrmMessageHelper;
import com.jbtits.github2telegram.helpers.TlgrmMetaHelper;
import com.jbtits.github2telegram.service.cfg.ConfigurationKeyValueService;
import com.jbtits.github2telegram.service.cfg.ConfigurationPersistenceService;
import com.jbtits.github2telegram.service.cfg.ConfigurationWizardService;
import com.jbtits.github2telegram.service.tlgrm.impl.cfg.TlgrmConfigurationMessagesKeyValueService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Set;

@Slf4j
@Component
public class StartConfigurationEventListener
    extends AbstractTlgrmCmdListener<TlgrmMessageContext, StartConfigurationMessageEvent> {

  private final ConfigurationPersistenceService<TlgrmChatContext, TlgrmUserContext> configurationPersistenceService;
  private final ConfigurationKeyValueService<Long, TlgrmChatContext, TlgrmUserContext> configurationKeyValueService;
  private final ConfigurationWizardService<TlgrmChatContext, TlgrmUserContext> configurationWizardService;
  private final TlgrmConfigurationMessagesKeyValueService tlgrmConfigurationMessagesKeyValueService;

  StartConfigurationEventListener(final TlgrmMetaHelper tlgrmMetaHelper,
                                  final TlgrmMessageHelper tlgrmMessageHelper,
                                  final ConfigurationPersistenceService<TlgrmChatContext, TlgrmUserContext> configurationPersistenceService,
                                  final ConfigurationKeyValueService<Long, TlgrmChatContext, TlgrmUserContext> configurationKeyValueService,
                                  final ConfigurationWizardService<TlgrmChatContext, TlgrmUserContext> configurationWizardService,
                                  final TlgrmConfigurationMessagesKeyValueService tlgrmConfigurationMessagesKeyValueService) {
    super(tlgrmMetaHelper, tlgrmMessageHelper);
    this.configurationPersistenceService = configurationPersistenceService;
    this.configurationKeyValueService = configurationKeyValueService;
    this.configurationWizardService = configurationWizardService;
    this.tlgrmConfigurationMessagesKeyValueService = tlgrmConfigurationMessagesKeyValueService;
  }

  @Override
  protected @NonNull StartConfigurationMessageEvent convertToEvent(@NonNull Message message)
      throws TlgrmListenerException {
    final var chat = message.getChat();
    final long chatId = chat.getId();
    final var from = message.getFrom();
    if (from == null) {
      throw new TlgrmListenerException("Message hasn't 'from' object");
    }
    if (from.getIsBot()) {
      throw new TlgrmListenerException("Bot message. Skipped");
    }
    return new StartConfigurationMessageEvent(chatId, from.getId(), message.getMessageId());
  }

  @Override
  @Transactional(readOnly = true)
  protected void on(@NonNull StartConfigurationMessageEvent event) {
    final var context = event.getContext();
    final var emptyConfiguration
        = this.configurationWizardService.generateEmptyConfiguration(context);
    final var tribeConfiguration
        = this.configurationKeyValueService.get(context.getChatId())
          .or(() -> this.configurationPersistenceService.get(context))
          .orElse(emptyConfiguration);
    this.configurationKeyValueService.put(context.getChatId(), tribeConfiguration);
    this.tlgrmConfigurationMessagesKeyValueService.putConfigurationMessageId(context.getMessageId());
    this.tlgrmMessageHelper.sendTribeConfigurationMessage(context, tribeConfiguration);
  }

  @Override
  protected Logger getLogger() {
    return log;
  }

  @Override
  protected @NonNull Set<TlgrmBotCmd> getCommands() {
    return Set.of(TlgrmBotCmd.START, TlgrmBotCmd.CONFIG);
  }
}
