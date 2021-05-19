package com.jbtits.github2telegram.listener.tlgrm.msg;

import com.jbtits.github2telegram.domain.dto.tlgrm.AbstractTlgrmContext;
import com.jbtits.github2telegram.domain.event.AbstractEvent;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmListenerException;
import com.jbtits.github2telegram.helpers.TlgrmMessageHelper;
import com.jbtits.github2telegram.helpers.TlgrmMetaHelper;
import com.jbtits.github2telegram.listener.tlgrm.AbstractTlgrmListener;
import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public abstract class AbstractTlgrmMessageListener<C extends AbstractTlgrmContext, E extends AbstractEvent<C>>
    extends AbstractTlgrmListener<Message, C, E> {

  protected AbstractTlgrmMessageListener(@NonNull final TlgrmMetaHelper tlgrmMetaHelper,
                                         @NonNull final TlgrmMessageHelper tlgrmMessageHelper) {
    super(tlgrmMetaHelper, tlgrmMessageHelper);
  }

  @Override
  protected Optional<Message> extractSrc(@NonNull Update update) {
    final var msg = update.getMessage();
    return msg != null ? Optional.of(msg) : Optional.empty();
  }

  @Override
  protected void beforeConvert(@NonNull Message message) throws TlgrmListenerException {
    AbstractTlgrmMessageListener.isValidMessage(message);
  }

  protected static void hasText(@NonNull final Message msg) throws TlgrmListenerException {
    if (!msg.hasText()) {
      throw new TlgrmListenerException("Message doesn't has text");
    }
  }

  protected static void isValidMessage(@NonNull Message msg) throws TlgrmListenerException {
    if (msg.getChat() == null) {
      throw new TlgrmListenerException("Invalid message. Basic checks failed");
    }
  }
}
