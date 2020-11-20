package com.jbtits.github2telegram;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbtits.github2telegram.configuration.properties.BotApiProperties;
import com.jbtits.github2telegram.helpers.JsonHelper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class G2TBot extends TelegramLongPollingBot {

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
        log.info("New update: {}", this.jsonHelper.toPrettyString(update));
        final Message inMessage = update.getMessage();
        if (Objects.isNull(inMessage)) {
            log.warn("No message has been obtained from update {}. It may be an edited_message",
                    this.jsonHelper.toPrettyString(update));
            return;
        }
        final SendMessage sendMessageObj = SendMessage.builder()
                .chatId(inMessage.getChatId().toString())
                .text(inMessage.getText())
                .build();
        this.sendMessage(sendMessageObj);
    }

    private void sendMessage(SendMessage sendMessageObj) {
        try {
            this.execute(sendMessageObj);
        } catch (TelegramApiException e) {
            log.error("SendMessage error occurs", e);
            throw new RuntimeException(e);
        }
    }
}
