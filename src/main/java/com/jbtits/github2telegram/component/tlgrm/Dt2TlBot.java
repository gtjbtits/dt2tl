package com.jbtits.github2telegram.component.tlgrm;

import com.jbtits.github2telegram.configuration.properties.BotApiProperties;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmCallbackContext;
import com.jbtits.github2telegram.helpers.JsonHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;

@Slf4j
@Component
@RequiredArgsConstructor
public class Dt2TlBot extends TelegramLongPollingBot implements TlgrmSender {

    private final ApplicationEventPublisher publisher;
    private final BotApiProperties botApiProperties;
    private final JsonHelper jsonHelper;

    @Override
    public String getBotUsername() {
        return botApiProperties.getUsername();
    }

    @Override
    public String getBotToken() {
        return botApiProperties.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.trace("New update: {}", this.jsonHelper.toPrettyString(update));
        publisher.publishEvent(update);
    }

    @Override
    public void answerCallbackQuery(@NonNull TlgrmCallbackContext context) {
        final AnswerCallbackQuery answerCallbackQuery = AnswerCallbackQuery.builder()
            .callbackQueryId(context.getCallbackId())
            .build();
        this.safeExecute(answerCallbackQuery);
    }

    @Override
    public <T extends Serializable, M extends BotApiMethod<T>> T safeExecute(M botApiMethod) {
        log.trace("Sending message to the Telegram API {}", this.jsonHelper.toPrettyString(botApiMethod));
        try {
            return this.execute(botApiMethod);
        } catch (TelegramApiException e) {
            log.error("SendMessage error occurs", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Chat getChatMetadata(long chatId) {
        final var getChat = GetChat.builder()
            .chatId(String.valueOf(chatId))
            .build();
        return this.safeExecute(getChat);
    }

    @Override
    public ChatMember getChatMemberMetadata(final long chatId, final long userId) {
        final var getChatMember = GetChatMember.builder()
            .chatId(String.valueOf(chatId))
            .userId(userId)
            .build();
        return this.safeExecute(getChatMember);
    }
}
