package com.jbtits.github2telegram.listener.tlgrm.msg.cmd;

import com.jbtits.github2telegram.domain.dto.tlgrm.AbstractTlgrmContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmBotCmd;
import com.jbtits.github2telegram.domain.event.tlgrm.msg.cmd.AbstractTlgrmCmdEvent;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmListenerException;
import com.jbtits.github2telegram.helpers.TlgrmMessageHelper;
import com.jbtits.github2telegram.helpers.TlgrmMetaHelper;
import com.jbtits.github2telegram.listener.tlgrm.msg.AbstractTlgrmMessageListener;
import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Set;

abstract class AbstractTlgrmCmdListener<C extends AbstractTlgrmContext, E extends AbstractTlgrmCmdEvent<C>>
    extends AbstractTlgrmMessageListener<C, E> {

  protected AbstractTlgrmCmdListener(@NonNull final TlgrmMetaHelper tlgrmMetaHelper,
                                     @NonNull final TlgrmMessageHelper tlgrmMessageHelper) {
    super(tlgrmMetaHelper, tlgrmMessageHelper);
  }

  @Override
  protected void beforeConvert(@NonNull Message message) throws TlgrmListenerException {
    super.beforeConvert(message);
    if (!message.isCommand()) {
      throw new TlgrmListenerException("Not a bot command");
    }
    for (TlgrmBotCmd cmd: this.getCommands()) {
      if (this.isContainsCommand(message, cmd.getCmd())) {
        return;
      }
    }
    throw new TlgrmListenerException("Not my command. Skipped");
  }

  @NonNull
  protected abstract Set<TlgrmBotCmd> getCommands();

  protected boolean isContainsCommand(
      @NonNull final Message msg,
      @NonNull final String command) throws TlgrmListenerException {
    AbstractTlgrmMessageListener.hasText(msg);
    final String text = msg.getText();
    return this.tlgrmMetaHelper.textContainsCommand(text, command);
  }
}
