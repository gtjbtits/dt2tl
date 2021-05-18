package com.jbtits.github2telegram.listener.tlgrm;

import com.jbtits.github2telegram.domain.dto.tlgrm.AbstractTlgrmContext;
import com.jbtits.github2telegram.domain.event.AbstractEvent;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmMessageConversionToEventException;
import com.jbtits.github2telegram.helpers.TlgrmHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.List;

@RequiredArgsConstructor
abstract class AbstractTlgrmMessageListener<C extends AbstractTlgrmContext, E extends AbstractEvent<C>> {

  protected static final String MESSAGE_ENTITY_URL_TYPE = "url";
  protected static final String MESSAGE_ENTITY_CMD_TYPE = "bot_command";

  protected final TlgrmHelper tlgrmHelper;

  @EventListener
  public final void onMessage(@NonNull final Message message) {
    final E event;
    try {
      AbstractTlgrmMessageListener.isInvalidMessage(message);
      event = this.convertToEvent(message);
    } catch (final TlgrmMessageConversionToEventException e) {
      if (e.getCause() == null && e.getMessage() != null) {
        this.getLogger().debug("Message ignored, cause it's not my. Reason: {}", e.getMessage());
      } else {
        this.getLogger().error("Message conversion fails with exception", e);
      }
      return;
    }
    this.on(event);
  }

  @NonNull
  protected abstract E convertToEvent(@NonNull final Message message) throws TlgrmMessageConversionToEventException;

  protected abstract void on(@NonNull final E event);

  protected abstract Logger getLogger();

  protected static void hasType(final List<MessageEntity> entityList, @NonNull final String type)
      throws TlgrmMessageConversionToEventException {
    if (entityList == null
        || entityList.stream()
        .noneMatch(messageEntity -> messageEntity.getType().equals(type))) {
      throw new TlgrmMessageConversionToEventException("Message hasn't type " + type);
    }
  }

  protected static void hasText(final String text) throws TlgrmMessageConversionToEventException {
    if (text == null) {
      throw new TlgrmMessageConversionToEventException("Text is null");
    }
  }

  protected void textContainsCommand(
      final String text,
      @NonNull String command) throws TlgrmMessageConversionToEventException {
    AbstractTlgrmMessageListener.hasText(text);
    if (!this.tlgrmHelper.textContainsCommand(text, command)) {
      throw new TlgrmMessageConversionToEventException(
          String.format("Text command mismatch exception. Actual %s, expected %s", text, command));
    }
  }

  private static void isInvalidMessage(@NonNull Message msg) throws TlgrmMessageConversionToEventException {
    if (msg.getChat() == null) {
      throw new TlgrmMessageConversionToEventException("Invalid message. Basic checks failed");
    }
  }
}
