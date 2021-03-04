package com.jbtits.github2telegram.component;

import com.jbtits.github2telegram.configuration.properties.BotApiProperties;
import com.jbtits.github2telegram.domain.event.MentionAllEvent;
import com.jbtits.github2telegram.domain.event.NewUrlMessageEvent;
import com.jbtits.github2telegram.helpers.JsonHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class G2TBot extends TelegramLongPollingBot {

    private static final String MESSAGE_ENTITY_URL_TYPE = "url";
    private static final String MESSAGE_ENTITY_CMD_TYPE = "bot_command";

    private static final String ALL_CMD = "/all";

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
        final Message msg = update.getMessage();
        this.parseUrlMessage(msg).ifPresent(publisher::publishEvent);
        this.parseMentionAllCmd(msg).ifPresent(publisher::publishEvent);
    }

    public void sendMessage(String text, String chatId) {
        final SendMessage sendMessageObj = SendMessage.builder()
            .chatId(chatId)
            .text(text)
            .build();
        try {
            this.execute(sendMessageObj);
        } catch (TelegramApiException e) {
            log.error("SendMessage error occurs", e);
            throw new RuntimeException(e);
        }
    }

    private Optional<NewUrlMessageEvent> parseUrlMessage(Message msg) {
        if (msg == null
            || msg.getText() == null) {
            return Optional.empty();
        }
        if (this.hasNotType(msg.getEntities(), MESSAGE_ENTITY_URL_TYPE)) {
            return Optional.empty();
        }
        final User from = msg.getFrom();
        if (from == null || from.getUserName() == null) {
            return Optional.empty();
        }
        final Chat chat = msg.getChat();
        if (chat == null) {
            return Optional.empty();
        }
        final String url = msg.getText();
        final String username = from.getUserName();
        final String chatId = chat.getId().toString();
        return Optional.of(new NewUrlMessageEvent(url, username, chatId));
    }

    private Optional<MentionAllEvent> parseMentionAllCmd(Message msg) {
        if (msg == null
            || msg.getText() == null) {
            return Optional.empty();
        }
        if (this.hasNotType(msg.getEntities(), MESSAGE_ENTITY_CMD_TYPE)) {
            return Optional.empty();
        }
        final Chat chat = msg.getChat();
        if (chat == null) {
            return Optional.empty();
        }
        final String text = msg.getText();
        final String chatId = chat.getId().toString();
        return text.equals(ALL_CMD) || text.equals(ALL_CMD + '@' + botApiProperties.getUsername())
            ? Optional.of(new MentionAllEvent(chatId))
            : Optional.empty();
    }

    private boolean hasNotType(List<MessageEntity> entityList, String type) {
        return entityList == null
            || entityList.stream()
                .noneMatch(messageEntity -> messageEntity.getType().equals(type));
    }
}
