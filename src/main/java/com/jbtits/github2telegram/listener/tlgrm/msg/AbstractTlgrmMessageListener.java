package com.jbtits.github2telegram.listener.tlgrm.msg;

import com.jbtits.github2telegram.domain.dto.tlgrm.AbstractTlgrmContext;
import com.jbtits.github2telegram.domain.event.AbstractEvent;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmEventProcessingException;
import com.jbtits.github2telegram.helpers.TlgrmHelper;
import com.jbtits.github2telegram.listener.tlgrm.AbstractTlgrmListener;
import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

public abstract class AbstractTlgrmMessageListener<C extends AbstractTlgrmContext, E extends AbstractEvent<C>>
    extends AbstractTlgrmListener<Message, C, E> {

  protected AbstractTlgrmMessageListener(TlgrmHelper tlgrmHelper) {
    super(tlgrmHelper);
  }

  @Override
  protected Optional<Message> extractSrc(@NonNull Update update) {
    final var msg = update.getMessage();
    return msg != null ? Optional.of(msg) : Optional.empty();
  }

  @Override
  protected void onSrc(@NonNull Message message) {
      final E event;
      try {
        AbstractTlgrmMessageListener.isValidMessage(message);
        event = this.convertToEvent(message);
      } catch (final TlgrmEventProcessingException e) {
        if (e.getCause() == null && e.getMessage() != null) {
          this.getLogger().debug("Message ignored, cause it's not my. Reason: {}", e.getMessage());
        } else {
          this.getLogger().error("Message conversion fails with exception", e);
        }
        return;
      }
      this.on(event);
  }

  protected static void hasType(final List<MessageEntity> entityList, @NonNull final String type)
      throws TlgrmEventProcessingException {
    if (entityList == null
        || entityList.stream()
        .noneMatch(messageEntity -> messageEntity.getType().equals(type))) {
      throw new TlgrmEventProcessingException("Message hasn't type " + type);
    }
  }

  protected void textContainsCommand(
      final String text,
      @NonNull String command) throws TlgrmEventProcessingException {
    AbstractTlgrmMessageListener.hasText(text);
    if (!this.tlgrmHelper.textContainsCommand(text, command)) {
      throw new TlgrmEventProcessingException(
          String.format("Text command mismatch exception. Actual %s, expected %s", text, command));
    }
  }

  protected static void hasText(final String text) throws TlgrmEventProcessingException {
    if (text == null) {
      throw new TlgrmEventProcessingException("Text is null");
    }
  }

  protected static void isValidMessage(@NonNull Message msg) throws TlgrmEventProcessingException {
    if (msg.getChat() == null) {
      throw new TlgrmEventProcessingException("Invalid message. Basic checks failed");
    }
  }
}
