package com.jbtits.github2telegram.listener.tlgrm.cbq;

import com.jbtits.github2telegram.domain.dto.tlgrm.AbstractTlgrmContext;
import com.jbtits.github2telegram.domain.event.tlgrm.cbq.AbstractTlgrmCallbackEvent;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmListenerException;
import com.jbtits.github2telegram.helpers.TlgrmMessageHelper;
import com.jbtits.github2telegram.helpers.TlgrmMetaHelper;
import com.jbtits.github2telegram.listener.tlgrm.AbstractTlgrmListener;
import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

abstract class AbstractTlgrmCallbackQueryListener<C extends AbstractTlgrmContext,
    E extends AbstractTlgrmCallbackEvent<C>>
    extends AbstractTlgrmListener<CallbackQuery, C, E> {

  protected AbstractTlgrmCallbackQueryListener(@NonNull final TlgrmMetaHelper tlgrmMetaHelper,
                                               @NonNull final TlgrmMessageHelper tlgrmMessageHelper) {
    super(tlgrmMetaHelper, tlgrmMessageHelper);
  }

  @Override
  protected Optional<CallbackQuery> extractSrc(@NonNull Update update) {
    final var callbackQuery = update.getCallbackQuery();
    return callbackQuery != null ? Optional.of(callbackQuery) : Optional.empty();
  }

  @Override
  protected void beforeConvert(@NonNull CallbackQuery callbackQuery) throws TlgrmListenerException {
    this.compareData(callbackQuery);
  }

  protected abstract TlgrmCallbackQueryAction getAction();

  protected final void compareData(@NonNull CallbackQuery callbackQuery) throws TlgrmListenerException {
    final String data = callbackQuery.getData();
    if (data == null) {
      throw new TlgrmListenerException("Callback data is null");
    }
    final var callbackQueryData = TlgrmCallbackQueryData.decode(data);
    if (!callbackQueryData.getAction().equals(this.getAction())) {
      throw new TlgrmListenerException(
          String.format("Callback data mismatch. Obtained data is %s", data));
    }
  }
}
