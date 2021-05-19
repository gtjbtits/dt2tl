package com.jbtits.github2telegram.component.tlgrm;

import com.jbtits.github2telegram.configuration.properties.BotApiProperties;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmCallbackContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.helpers.JsonHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
    public void sendMessage(@NonNull String text, @NonNull TlgrmChatContext context) {
        final var sendMessage = SendMessage.builder()
            .chatId(String.valueOf(context.getChatId()))
            .text(text)
            .build();
        this.sendMessage(sendMessage);
    }

    @Override
    public void sendMessage(@NonNull SendMessage sendMessage) {
        this.safeExecute(sendMessage);
    }

    @Override
    public void answerCallbackQuery(@NonNull TlgrmCallbackContext context) {
        final AnswerCallbackQuery answerCallbackQuery = AnswerCallbackQuery.builder()
            .callbackQueryId(context.getCallbackId())
            .build();
        this.safeExecute(answerCallbackQuery);
    }

    private void safeExecute(BotApiMethod<?> botApiMethod) {
        try {
            this.execute(botApiMethod);
        } catch (TelegramApiException e) {
            log.error("SendMessage error occurs", e);
            throw new RuntimeException(e);
        }
    }
}
