package com.jbtits.github2telegram.listener.tlgrm.cbq;

import com.jbtits.github2telegram.component.tlgrm.TlgrmSender;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmCallbackContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.domain.event.tlgrm.cbq.ConfigBackCallbackEvent;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmListenerException;
import com.jbtits.github2telegram.helpers.TlgrmMessageHelper;
import com.jbtits.github2telegram.helpers.TlgrmMetaHelper;
import com.jbtits.github2telegram.service.cfg.ConfigurationKeyValueService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Slf4j
@Service
public class ConfigBackCallbackListener extends AbstractTlgrmCallbackQueryListener<TlgrmCallbackContext, ConfigBackCallbackEvent> {

  private final ConfigurationKeyValueService<Long, TlgrmChatContext, TlgrmUserContext> configurationKeyValueService;
  private final TlgrmSender tlgrmSender;

  protected ConfigBackCallbackListener(final TlgrmMetaHelper tlgrmMetaHelper,
                                       final TlgrmMessageHelper tlgrmMessageHelper,
                                       final ConfigurationKeyValueService<Long, TlgrmChatContext, TlgrmUserContext> configurationKeyValueService,
                                       final TlgrmSender tlgrmSender) {
    super(tlgrmMetaHelper, tlgrmMessageHelper);
    this.configurationKeyValueService = configurationKeyValueService;
    this.tlgrmSender = tlgrmSender;
  }

  @Override
  protected @NonNull ConfigBackCallbackEvent convertToEvent(final @NonNull CallbackQuery src)
      throws TlgrmListenerException {
    return new ConfigBackCallbackEvent(tlgrmMetaHelper.extractCallbackContext(src));
  }

  @Override
  protected void on(final @NonNull ConfigBackCallbackEvent event) {
    final var context = event.getContext();
    this.configurationKeyValueService.get(context.getChatId()).ifPresent(tribeConfiguration -> {
      this.tlgrmMessageHelper.updateTribeConfigurationMessage(context, tribeConfiguration);
    });
    this.tlgrmSender.answerCallbackQuery(context);
  }

  @Override
  protected Logger getLogger() {
    return log;
  }

  @Override
  protected TlgrmCallbackQueryAction getAction() {
    return TlgrmCallbackQueryAction.CONFIG_BACK;
  }
}
