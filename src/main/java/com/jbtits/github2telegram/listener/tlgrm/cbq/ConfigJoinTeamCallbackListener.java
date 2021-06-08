package com.jbtits.github2telegram.listener.tlgrm.cbq;

import com.jbtits.github2telegram.component.tlgrm.TlgrmSender;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmCallbackContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.domain.event.tlgrm.cbq.ConfigTeamJoinCallbackEvent;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmListenerException;
import com.jbtits.github2telegram.helpers.TlgrmMessageHelper;
import com.jbtits.github2telegram.helpers.TlgrmMetaHelper;
import com.jbtits.github2telegram.service.cfg.ConfigurationKeyValueService;
import com.jbtits.github2telegram.service.cfg.ConfigurationWizardService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Slf4j
@Service
public class ConfigJoinTeamCallbackListener
    extends AbstractTlgrmCallbackQueryListener<TlgrmCallbackContext, ConfigTeamJoinCallbackEvent> {

  private final TlgrmSender tlgrmSender;
  private final ConfigurationWizardService<TlgrmChatContext, TlgrmUserContext> configurationWizardService;
  private final ConfigurationKeyValueService<Long, TlgrmChatContext, TlgrmUserContext> configurationKeyValueService;

  protected ConfigJoinTeamCallbackListener(final TlgrmMetaHelper tlgrmMetaHelper,
                                           final TlgrmMessageHelper tlgrmMessageHelper,
                                           final TlgrmSender tlgrmSender,
                                           final ConfigurationWizardService<TlgrmChatContext, TlgrmUserContext> configurationWizardService,
                                           final ConfigurationKeyValueService<Long, TlgrmChatContext, TlgrmUserContext> configurationKeyValueService) {
    super(tlgrmMetaHelper, tlgrmMessageHelper);
    this.tlgrmSender = tlgrmSender;
    this.configurationWizardService = configurationWizardService;
    this.configurationKeyValueService = configurationKeyValueService;
  }

  @Override
  protected @NonNull ConfigTeamJoinCallbackEvent convertToEvent(final @NonNull CallbackQuery src)
      throws TlgrmListenerException {
    final String data = src.getData();
    final var callbackQueryData = TlgrmCallbackQueryData.decode(data);
    return new ConfigTeamJoinCallbackEvent(tlgrmMetaHelper.extractCallbackContext(src), callbackQueryData.getArgs());
  }

  @Override
  protected void on(final @NonNull ConfigTeamJoinCallbackEvent event) {
    final var context = event.getContext();
    final var actionArgs = event.getActionArgs();
    if (actionArgs.length < 1 || !(actionArgs[0] instanceof String)) {
      throw new IllegalStateException("No team name in action args or it's not a String");
    }
    final var teamName = (String) actionArgs[0];
    this.configurationKeyValueService.get(context.getChatId()).ifPresent(tribeConfiguration -> {
      final var teamWasChanged = this.configurationWizardService
          .changeTeam(teamName, tribeConfiguration, context);
      if (teamWasChanged) {
        this.tlgrmMessageHelper.updateTeamsConfigurationMessage(context, tribeConfiguration);
      }
    });
    tlgrmSender.answerCallbackQuery(context);
  }

  @Override
  protected Logger getLogger() {
    return log;
  }

  @Override
  protected TlgrmCallbackQueryAction getAction() {
    return TlgrmCallbackQueryAction.CONFIG_TEAM_JOIN;
  }
}
