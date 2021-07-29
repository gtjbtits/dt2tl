package com.jbtits.github2telegram.listener.tlgrm.msg;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.domain.event.tlgrm.msg.TlgrmAnyUsersMessageEvent;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmListenerException;
import com.jbtits.github2telegram.helpers.TlgrmMessageHelper;
import com.jbtits.github2telegram.helpers.TlgrmMetaHelper;
import com.jbtits.github2telegram.service.tlgrm.TlgrmMetaService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
public class TlgrmAnyUsersMessageListener extends AbstractTlgrmMessageListener<TlgrmUserContext, TlgrmAnyUsersMessageEvent> {

  private final TlgrmMetaService tlgrmMetaService;

  protected TlgrmAnyUsersMessageListener(
      final @NonNull TlgrmMetaHelper tlgrmMetaHelper,
      final @NonNull TlgrmMessageHelper tlgrmMessageHelper,
      final @NonNull TlgrmMetaService tlgrmMetaService) {
    super(tlgrmMetaHelper, tlgrmMessageHelper);
    this.tlgrmMetaService = tlgrmMetaService;
  }

  @Override
  protected @NonNull TlgrmAnyUsersMessageEvent convertToEvent(final @NonNull Message src)
      throws TlgrmListenerException {
    final var chat = src.getChat();
    final long chatId = chat.getId();
    final var from = src.getFrom();
    if (from == null) {
      throw new TlgrmListenerException("Message hasn't 'from' object");
    }
    if (from.getIsBot()) {
      throw new TlgrmListenerException("Bot message. Skipped");
    }
    return new TlgrmAnyUsersMessageEvent(new TlgrmUserContext(chatId, from.getId()));
  }

  @Override
  protected void on(final @NonNull TlgrmAnyUsersMessageEvent event) {
    final TlgrmUserContext userContext = event.getContext();
    this.tlgrmMetaService.obtainChatMemberMetadata(userContext.getChatId(), userContext.getUserId());
  }

  @Override
  protected Logger getLogger() {
    return log;
  }
}
