package com.jbtits.github2telegram.helpers.impl;

import com.jbtits.github2telegram.configuration.properties.BotApiProperties;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmCallbackContext;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmListenerException;
import com.jbtits.github2telegram.helpers.TlgrmMetaHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.ChatMember;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

import static com.jbtits.github2telegram.util.TlgrmUtils.TELEGRAM_AT_PREFIX;

@Component
@RequiredArgsConstructor
public class DefaultTlgrmMetaHelper implements TlgrmMetaHelper {

  private final BotApiProperties botApiProperties;

  @NonNull
  public boolean textContainsCommand(@NonNull String text, @NonNull String command) {
    return text.equals(command) || text.equals(command + TELEGRAM_AT_PREFIX + botApiProperties.getUsername());
  }

  @NonNull
  public TlgrmCallbackContext extractCallbackContext(@NonNull CallbackQuery callbackQuery)
      throws TlgrmListenerException {
    final var message = callbackQuery.getMessage();
    if (message == null) {
      throw new TlgrmListenerException("Message is null");
    }
    final var messageId = message.getMessageId();
    if (messageId == null) {
      throw new TlgrmListenerException("Src messageId is null");
    }
    final var chat = message.getChat();
    if (chat == null) {
      throw new TlgrmListenerException("Chat is null");
    }
    final long chatId = chat.getId();
    final User from = callbackQuery.getFrom();
    if (from == null) {
      throw new TlgrmListenerException("User (from) is null");
    }
    final long userId = from.getId();
    final String callbackId = callbackQuery.getId();
    if (callbackId == null) {
      throw new TlgrmListenerException("Callback id is null");
    }
    return new TlgrmCallbackContext(chatId, userId, messageId, callbackId);
  }

  @Override
  public @NonNull String extractName(@NonNull final ChatMember chatMember) {
    final var user = chatMember.getUser();
    if (user == null) {
      throw new IllegalStateException("ChatMember hasn't User object");
    }
    return user.getLastName() == null || user.getLastName().isBlank()
        ? user.getFirstName()
        : user.getFirstName() + " " + user.getLastName();
  }

  @Override
  public Optional<String> extractUsername(@NonNull ChatMember chatMember) {
    final var user = chatMember.getUser();
    if (user == null) {
      throw new IllegalStateException("ChatMember hasn't User object");
    }
    return user.getUserName() != null && !user.getUserName().isBlank()
        ? Optional.of(TELEGRAM_AT_PREFIX + user.getUserName())
        : Optional.empty();
  }

  @NonNull
  @Override
  public String constructUserNameWithUsername(@NonNull ChatMember chatMember) {
    final var sb = new StringBuilder();
    sb.append(this.extractName(chatMember));
    this.extractUsername(chatMember).ifPresent(userName -> {
      sb.append(" (");
      sb.append(userName);
      sb.append(")");
    });
    return sb.toString();
  }
}
