package com.jbtits.github2telegram.listener.tlgrm.msg.reply;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmMessageContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.domain.event.tlgrm.msg.reply.AddTeamConfigurationReplyEvent;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmListenerException;
import com.jbtits.github2telegram.helpers.TlgrmMessageHelper;
import com.jbtits.github2telegram.helpers.TlgrmMetaHelper;
import com.jbtits.github2telegram.service.cfg.ConfigurationKeyValueService;
import com.jbtits.github2telegram.service.cfg.ConfigurationWizardService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Service
public class AddTeamConfigurationEventListener extends AbstractTlgrmMessageReplyListener<TlgrmMessageContext, AddTeamConfigurationReplyEvent> {

  private static final int TLGRM_CALLBACK_DATA_MAX_LENGTH_BYTES = 64;

  private final ConfigurationKeyValueService<Long, TlgrmChatContext, TlgrmUserContext> configurationKeyValueService;
  private final ConfigurationWizardService<TlgrmChatContext, TlgrmUserContext> configurationWizardService;

  protected AddTeamConfigurationEventListener(final TlgrmMetaHelper tlgrmMetaHelper,
                                              final TlgrmMessageHelper tlgrmMessageHelper,
                                              final ConfigurationKeyValueService<Long, TlgrmChatContext, TlgrmUserContext> configurationKeyValueService,
                                              final ConfigurationWizardService<TlgrmChatContext, TlgrmUserContext> configurationWizardService) {
    super(tlgrmMetaHelper, tlgrmMessageHelper);
    this.configurationKeyValueService = configurationKeyValueService;
    this.configurationWizardService = configurationWizardService;
  }

  @Override
  protected @NonNull AddTeamConfigurationReplyEvent convertToEvent(final @NonNull Message src)
      throws TlgrmListenerException {
    final var chat = src.getChat();
    final long chatId = chat.getId();
    final var replyToMessage = src.getReplyToMessage();

    final var from = src.getFrom();
    if (from == null) {
      throw new TlgrmListenerException("Message hasn't 'from' object");
    }
    if (from.getIsBot()) {
      throw new TlgrmListenerException("Bot message. Skipped");
    }
    if (!src.hasText()) {
      throw new TlgrmListenerException("Team name is empty or null");
    }
    final var teamName = src.getText().trim();
    return new AddTeamConfigurationReplyEvent(new TlgrmMessageContext(chatId, from.getId(),
        replyToMessage.getMessageId()), teamName);
  }

  @Override
  protected void on(final @NonNull AddTeamConfigurationReplyEvent event) {
    final var context = event.getContext();
    this.configurationKeyValueService.get(context.getChatId()).ifPresent(tribeConfiguration -> {
      final var teamName = event.getTeamName();
      if (teamName.getBytes().length > TLGRM_CALLBACK_DATA_MAX_LENGTH_BYTES
          || !this.configurationWizardService.isTeamNameUnique(teamName, tribeConfiguration)) {
        tlgrmMessageHelper.sendError("tlgrm_err_config_add_team", context.getChatId());
        return;
      }
      this.configurationWizardService.addTeam(teamName, tribeConfiguration);
      tlgrmMessageHelper.updateTribeConfigurationMessage(context, tribeConfiguration);
    });
  }

  @Override
  protected Logger getLogger() {
    return log;
  }
}
