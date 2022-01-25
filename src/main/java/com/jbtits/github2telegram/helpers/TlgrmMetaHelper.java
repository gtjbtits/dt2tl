package com.jbtits.github2telegram.helpers;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmCallbackContext;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmListenerException;
import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

import java.util.Optional;

public interface TlgrmMetaHelper {
  boolean textContainsCommand(@NonNull String text, @NonNull String command);

  @NonNull
  TlgrmCallbackContext extractCallbackContext(@NonNull CallbackQuery callbackQuery) throws TlgrmListenerException;

  @NonNull
  String extractName(@NonNull ChatMember chatMember);

  Optional<String> extractUsername(@NonNull ChatMember chatMember);

  @NonNull
  String constructUserNameWithUsername(@NonNull ChatMember chatMember);
}
