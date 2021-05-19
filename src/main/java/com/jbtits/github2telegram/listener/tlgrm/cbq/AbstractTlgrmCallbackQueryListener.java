package com.jbtits.github2telegram.listener.tlgrm.cbq;

import com.jbtits.github2telegram.domain.dto.tlgrm.AbstractTlgrmContext;
import com.jbtits.github2telegram.domain.event.AbstractEvent;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmEventProcessingException;
import com.jbtits.github2telegram.helpers.TlgrmHelper;
import com.jbtits.github2telegram.listener.tlgrm.AbstractTlgrmListener;
import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public abstract class AbstractTlgrmCallbackQueryListener<C extends AbstractTlgrmContext, E extends AbstractEvent<C>>
    extends AbstractTlgrmListener<CallbackQuery, C, E> {

  protected AbstractTlgrmCallbackQueryListener(TlgrmHelper tlgrmHelper) {
    super(tlgrmHelper);
  }

  @Override
  protected Optional<CallbackQuery> extractSrc(@NonNull Update update) {
    final var callbackQuery = update.getCallbackQuery();
    return callbackQuery != null ? Optional.of(callbackQuery) : Optional.empty();
  }

  @Override
  protected void onSrc(@NonNull CallbackQuery callbackQuery) {
    final E event;
    try {
      this.compareData(callbackQuery);
      event = this.convertToEvent(callbackQuery);
    } catch (final TlgrmEventProcessingException e) {
      if (e.getCause() == null && e.getMessage() != null) {
        this.getLogger().debug("CallbackQuery ignored, cause it's not my. Reason: {}", e.getMessage());
      } else {
        this.getLogger().error("CallbackQuery conversion fails with exception", e);
      }
      return;
    }
    this.on(event);
  }

  protected abstract String getData();

  protected final void compareData(@NonNull CallbackQuery callbackQuery) throws TlgrmEventProcessingException {
    final String data = callbackQuery.getData();
    if (data == null) {
      throw new TlgrmEventProcessingException("Callback data is null");
    }
    if (!data.equals(this.getData())) {
      throw new TlgrmEventProcessingException(
          String.format("Callback data mismatch. Expected %s, obtained %s", this.getData(), data));
    }
  }
}
