package com.jbtits.github2telegram.listener.tlgrm.msg;

import com.jbtits.github2telegram.configuration.properties.ChatPrProperties;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmMessageContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.domain.event.tlgrm.msg.TlgrmPrMessageEvent;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmListenerException;
import com.jbtits.github2telegram.domain.exception.tlgrm.cfg.TlgrmChatNotFoundException;
import com.jbtits.github2telegram.domain.exception.tlgrm.cfg.TlgrmFellowNotFoundException;
import com.jbtits.github2telegram.domain.exception.tlgrm.cfg.TlgrmUserNotFoundException;
import com.jbtits.github2telegram.helpers.MessageHelper;
import com.jbtits.github2telegram.helpers.TlgrmMessageHelper;
import com.jbtits.github2telegram.helpers.TlgrmMetaHelper;
import com.jbtits.github2telegram.persistence.entity.Fellow;
import com.jbtits.github2telegram.persistence.service.TribeService;
import com.jbtits.github2telegram.persistence.service.tlgrm.TlgrmUserService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Slf4j
@Service
public class TlgrmPrMessageListener extends AbstractTlgrmMessageListener<TlgrmMessageContext, TlgrmPrMessageEvent> {

  private static final String MESSAGE_ENTITY_TYPE_URL = "url";
  private final TribeService<TlgrmChatContext, TlgrmUserContext> tribeService;
  private final TlgrmUserService tlgrmUserService;
  private final MessageHelper messageHelper;
  private final ChatPrProperties properties;

  protected TlgrmPrMessageListener(final TlgrmMetaHelper tlgrmMetaHelper,
                                   final TlgrmMessageHelper tlgrmMessageHelper,
                                   final TribeService<TlgrmChatContext, TlgrmUserContext> tribeService,
                                   final TlgrmUserService tlgrmUserService,
                                   final MessageHelper messageHelper, ChatPrProperties properties) {
    super(tlgrmMetaHelper, tlgrmMessageHelper);
    this.tribeService = tribeService;
    this.tlgrmUserService = tlgrmUserService;
    this.messageHelper = messageHelper;
    this.properties = properties;
  }

  @Override
  protected @NonNull TlgrmPrMessageEvent convertToEvent(final @NonNull Message src) throws TlgrmListenerException {
    final var chat = src.getChat();
    final long chatId = chat.getId();
    final var from = src.getFrom();
    if (from == null) {
      throw new TlgrmListenerException("Message hasn't 'from' object");
    }
    if (from.getIsBot()) {
      throw new TlgrmListenerException("Bot message. Skipped");
    }
    final var messageEntities = src.getEntities();
    if (messageEntities == null || messageEntities.isEmpty()) {
      throw new TlgrmListenerException("Message hasn't messageEntities");
    }
    if (messageEntities.size() > 1) {
      throw new TlgrmListenerException("Too many messageEntities. Pr message must simple message with URL");
    }
    final var messageEntity = messageEntities.get(0);
    final var type = messageEntity.getType();
    final int offset = messageEntity.getOffset();
    final int length = messageEntity.getLength();
    if (!type.equals(MESSAGE_ENTITY_TYPE_URL)) {
      throw new TlgrmListenerException("Not an URL");
    }
    if (!src.hasText()) {
      throw new TlgrmListenerException("Message has no text");
    }
    final var text = src.getText();
    final var prefix = this.properties.getPrefix();
    if (!text.contains(prefix) || text.indexOf(prefix) >= offset) {
      throw new TlgrmListenerException("Wrong PR format. Prefix must be before the url");
    }
    final var url = text.substring(offset, offset + length);
    final var keyWords = this.properties.getKeyWords().stream()
        .filter(url::contains)
        .count();
    if (keyWords == 0) {
      throw new TlgrmListenerException("Url doesn't contains any vcs key words. Not a PR link (skipped)");
    }
    return new TlgrmPrMessageEvent(new TlgrmMessageContext(chatId, from.getId(), src.getMessageId()), url);
  }

  @Override
  @Transactional(readOnly = true)
  protected void on(final @NonNull TlgrmPrMessageEvent event) {
    final var context = event.getContext();
    final List<Fellow> reviewers;
    try {
      reviewers = this.tribeService.findReviewers(context);
    } catch (TlgrmChatNotFoundException e) {
      log.warn("Reviewers for tlgrmChat={} was not found. Because: {}", context.getChatId(), e.getMessage());
      this.tlgrmMessageHelper.sendError("tlgrm_err_pr_no_available_cfg", context.getChatId());
      return;
    } catch (TlgrmUserNotFoundException e) {
      this.tlgrmMessageHelper.sendError("tlgrm_err_pr_author_not_found", context.getChatId());
      return;
    } catch (TlgrmFellowNotFoundException e) {
      this.tlgrmMessageHelper.sendError("tlgrm_err_pr_you_are_not_in_cfg", context.getChatId());
      return;
    }
    if (reviewers.isEmpty()) {
      this.tlgrmMessageHelper.sendError("tlgrm_err_pr_no_available_reviewers", context.getChatId());
      return;
    }
    final var tlgrmUsersForFellows = this.tlgrmUserService.findTlgrmUsersForFellows(reviewers);
    this.tlgrmMessageHelper.sendReviewAnnounce(context, tlgrmUsersForFellows, event.getPrLink());
  }

  @Override
  protected Logger getLogger() {
    return log;
  }
}
