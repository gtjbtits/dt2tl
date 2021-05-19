package com.jbtits.github2telegram.helpers;

import com.jbtits.github2telegram.configuration.properties.BotApiProperties;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmCallbackContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmEventProcessingException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.jbtits.github2telegram.listener.tlgrm.cbq.TlgrmCallbackQueryData.CONFIG_CANCEL;
import static com.jbtits.github2telegram.listener.tlgrm.cbq.TlgrmCallbackQueryData.CONFIG_JOIN;
import static com.jbtits.github2telegram.listener.tlgrm.cbq.TlgrmCallbackQueryData.CONFIG_RESET;
import static com.jbtits.github2telegram.listener.tlgrm.cbq.TlgrmCallbackQueryData.CONFIG_SAVE;

@Component
@RequiredArgsConstructor
public class TlgrmHelper {

  private static final String TELEGRAM_AT_PREFIX = "@";

  private final DefaultAbsSender tlgrmBot;
  private final BotApiProperties botApiProperties;

  public boolean textContainsCommand(@NonNull String text, @NonNull String command) {
    return text.equals(command) || text.equals(command + TELEGRAM_AT_PREFIX + botApiProperties.getUsername());
  }

  @NonNull
  public TlgrmCallbackContext extractCallbackContext(@NonNull CallbackQuery callbackQuery) throws
      TlgrmEventProcessingException {
    final var message = callbackQuery.getMessage();
    if (message == null) {
      throw new TlgrmEventProcessingException("Message is null");
    }
    final var chat = message.getChat();
    if (chat == null) {
      throw new TlgrmEventProcessingException("Chat is null");
    }
    final long chatId = chat.getId();
    final User from = callbackQuery.getFrom();
    if (from == null) {
      throw new TlgrmEventProcessingException("User (from) is null");
    }
    final long userId = from.getId();
    final String callbackId = callbackQuery.getId();
    if (callbackId == null) {
      throw new TlgrmEventProcessingException("Callback id is null");
    }
    return new TlgrmCallbackContext(chatId, userId, callbackId);
  }

  @NonNull
  public SendMessage constructCfgMessage(@NonNull TlgrmChatContext context) {
    final List<InlineKeyboardButton> keyboardRow1 = new ArrayList<>();
    keyboardRow1.add(InlineKeyboardButton.builder().text("Join").callbackData(CONFIG_JOIN).build());
    final List<InlineKeyboardButton> keyboardRow2 = new ArrayList<>();
    keyboardRow2.add(InlineKeyboardButton.builder().text("Cancel").callbackData(CONFIG_CANCEL).build());
    keyboardRow2.add(InlineKeyboardButton.builder().text("Save").callbackData(CONFIG_SAVE).build());
    keyboardRow2.add(InlineKeyboardButton.builder().text("Reset").callbackData(CONFIG_RESET).build());
    final InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
        .keyboard(List.of(keyboardRow1, keyboardRow2))
        .build();
    final SendMessage sendMessage = SendMessage.builder()
        .chatId(String.valueOf(context.getChatId()))
        .text("config text")
        .replyMarkup(inlineKeyboardMarkup)
        .build();
    return sendMessage;
  }
}
