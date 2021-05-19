package com.jbtits.github2telegram.component.tlgrm;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmCallbackContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import lombok.NonNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface TlgrmSender {

  void sendMessage(@NonNull String text, @NonNull TlgrmChatContext context);

  void sendMessage(@NonNull SendMessage sendMessage);

  void answerCallbackQuery(@NonNull TlgrmCallbackContext context);
}
