package com.jbtits.github2telegram.component.tlgrm;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmCallbackContext;
import lombok.NonNull;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

import java.io.Serializable;

public interface TlgrmSender {

  void answerCallbackQuery(@NonNull TlgrmCallbackContext context);

  <T extends Serializable, M extends BotApiMethod<T>> T safeExecute(M botApiMethod);

  Chat getChatMetadata(long chatId);

  ChatMember getChatMemberMetadata(long chatId, long userId);
}
