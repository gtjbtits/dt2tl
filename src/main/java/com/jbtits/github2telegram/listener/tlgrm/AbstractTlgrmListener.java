package com.jbtits.github2telegram.listener.tlgrm;

import com.jbtits.github2telegram.domain.dto.tlgrm.AbstractTlgrmContext;
import com.jbtits.github2telegram.domain.event.AbstractEvent;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmListenerException;
import com.jbtits.github2telegram.helpers.TlgrmMessageHelper;
import com.jbtits.github2telegram.helpers.TlgrmMetaHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractTlgrmListener<S extends BotApiObject, C extends AbstractTlgrmContext,
    E extends AbstractEvent<C>> {

  protected final TlgrmMetaHelper tlgrmMetaHelper;
  protected final TlgrmMessageHelper tlgrmMessageHelper;

  @EventListener
  public final void onTelegramUpdate(@NonNull final Update update) {
    this.extractSrc(update).ifPresent(this::onSrc);
  }

  protected abstract Optional<S> extractSrc(@NonNull final Update update);

  protected void onSrc(@NonNull S src) {
    final E event;
    try {
      this.beforeConvert(src);
      event = this.convertToEvent(src);
    } catch (final TlgrmListenerException e) {
      if (e.getCause() == null && e.getMessage() != null) {
        this.getLogger().debug("Src ignored, cause it's not my. Reason: {}", e.getMessage());
      } else {
        this.getLogger().error("Src conversion fails with exception", e);
      }
      return;
    }
    this.on(event);
  }

  protected abstract void beforeConvert(@NonNull final S src) throws TlgrmListenerException;

  @NonNull
  protected abstract E convertToEvent(@NonNull final S src) throws TlgrmListenerException;

  protected abstract void on(@NonNull final E event);

  protected abstract Logger getLogger();
}
