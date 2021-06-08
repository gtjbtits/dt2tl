package com.jbtits.github2telegram.listener.tlgrm.msg.reply;

import com.jbtits.github2telegram.domain.dto.tlgrm.AbstractTlgrmContext;
import com.jbtits.github2telegram.domain.event.AbstractEvent;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmListenerException;
import com.jbtits.github2telegram.helpers.TlgrmMessageHelper;
import com.jbtits.github2telegram.helpers.TlgrmMetaHelper;
import com.jbtits.github2telegram.listener.tlgrm.msg.AbstractTlgrmMessageListener;
import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.Message;

public abstract class AbstractTlgrmMessageReplyListener<C extends AbstractTlgrmContext, E extends AbstractEvent<C>> extends AbstractTlgrmMessageListener<C, E> {

  protected AbstractTlgrmMessageReplyListener(final TlgrmMetaHelper tlgrmMetaHelper,
                                              final TlgrmMessageHelper tlgrmMessageHelper) {
    super(tlgrmMetaHelper, tlgrmMessageHelper);
  }

  @Override
  protected void beforeConvert(@NonNull final Message message) throws TlgrmListenerException {
    super.beforeConvert(message);
    if (message.getReplyToMessage() == null) {
      throw new TlgrmListenerException("Message hasn't MessageReplyTo. This is not reply message");
    }
  }
}
