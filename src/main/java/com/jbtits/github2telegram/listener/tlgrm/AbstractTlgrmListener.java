package com.jbtits.github2telegram.listener.tlgrm;

import com.jbtits.github2telegram.domain.dto.tlgrm.AbstractTlgrmContext;
import com.jbtits.github2telegram.domain.event.AbstractEvent;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmEventProcessingException;
import com.jbtits.github2telegram.helpers.TlgrmHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractTlgrmListener<S, C extends AbstractTlgrmContext, E extends AbstractEvent<C>> {

  protected static final String MESSAGE_ENTITY_URL_TYPE = "url";
  protected static final String MESSAGE_ENTITY_CMD_TYPE = "bot_command";

  protected final TlgrmHelper tlgrmHelper;

  @EventListener
  public final void onTelegramUpdate(@NonNull final Update update) {
    this.extractSrc(update).ifPresent(this::onSrc);
  }

  protected abstract Optional<S> extractSrc(@NonNull final Update update);

  protected abstract void onSrc(@NonNull S src);

  @NonNull
  protected abstract E convertToEvent(@NonNull final S src) throws TlgrmEventProcessingException;

  protected abstract void on(@NonNull final E event);

  protected abstract Logger getLogger();
}
