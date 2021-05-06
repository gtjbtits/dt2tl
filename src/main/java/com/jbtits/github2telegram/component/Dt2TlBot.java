package com.jbtits.github2telegram.component;

import com.jbtits.github2telegram.configuration.properties.BotApiProperties;
import com.jbtits.github2telegram.domain.event.MentionAllEvent;
import com.jbtits.github2telegram.domain.event.NewConfigEvent;
import com.jbtits.github2telegram.domain.event.NewUrlMessageEvent;
import com.jbtits.github2telegram.helpers.JsonHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Document;
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
public class Dt2TlBot extends TelegramLongPollingBot {

    private static final String TEXT_CMD_PREFIX = "!";
    private static final String NEW_CONFIG_TEXT_CMD = TEXT_CMD_PREFIX;

    private static final String CONFIG_MIME_TYPE = "application/x-yaml";
    private static final int CONFIG_SIZE_LIMIT_BYTES = 20_000; // Telegram bot restriction: https://core.telegram.org/bots/api#getfile

    private static final String MESSAGE_ENTITY_URL_TYPE = "url";
    private static final String MESSAGE_ENTITY_CMD_TYPE = "bot_command";

    private static final String ALL_TELEGRAM_CMD = "/all";

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
        this.parseNewConfigMessage(msg).ifPresent(publisher::publishEvent);
    }

    public void sendMessage(String text, long chatId) {
        final SendMessage sendMessageObj = SendMessage.builder()
            .chatId(String.valueOf(chatId))
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
        if (!Dt2TlBot.isInvalidMessage(msg)) {
            log.debug("Invalid message, cause default checks not passed: {}", msg);
            return Optional.empty();
        }
        if (this.hasNotType(msg.getEntities(), MESSAGE_ENTITY_URL_TYPE)) {
            log.debug("Not an URL message: {}", msg);
            return Optional.empty();
        }
        final User from = msg.getFrom();
        if (from == null || from.getUserName() == null) {
            log.debug("Can't find 'from': {}", msg);
            return Optional.empty();
        }
        final Chat chat = msg.getChat();
        if (msg.getText() == null) {
            log.debug("Message has no text: {}", msg);
            return Optional.empty();
        }
        final String url = msg.getText();
        final String username = from.getUserName();
        final long chatId = chat.getId();
        return Optional.of(new NewUrlMessageEvent(chatId, url, username));
    }

    private Optional<NewConfigEvent> parseNewConfigMessage(Message msg) {
        if (Dt2TlBot.isInvalidMessage(msg)) {
            log.debug("Invalid message, cause default checks not passed: {}", msg);
            return Optional.empty();
        }
        final long chatId = msg.getChat().getId();
        final String caption = msg.getCaption();
        if (caption == null || !caption.equals(NEW_CONFIG_TEXT_CMD)) {
            log.debug("Bad caption: {}", msg);
            return Optional.empty();
        }
        final Document document = msg.getDocument();
        if (document == null || document.getFileId() == null
            || document.getMimeType() == null || !document.getMimeType().equals(CONFIG_MIME_TYPE)
            || document.getFileSize() == null || document.getFileSize() > CONFIG_SIZE_LIMIT_BYTES) {
            log.debug("Bad document: {}", msg);
            return Optional.empty();
        }
        final String fileId = document.getFileId();
        return Optional.of(new NewConfigEvent(chatId, fileId));
    }

    private Optional<MentionAllEvent> parseMentionAllCmd(Message msg) {
        if (Dt2TlBot.isInvalidMessage(msg)) {
            log.debug("Invalid message, cause default checks not passed: {}", msg);
            return Optional.empty();
        }
        if (this.hasNotType(msg.getEntities(), MESSAGE_ENTITY_CMD_TYPE)) {
            log.debug("Not an CMD message: {}", msg);
            return Optional.empty();
        }
        final Chat chat = msg.getChat();
        if (msg.getText() == null) {
            log.debug("Message has no text: {}", msg);
            return Optional.empty();
        }
        final String text = msg.getText();
        final long chatId = chat.getId();
        return text.equals(ALL_TELEGRAM_CMD) || text.equals(ALL_TELEGRAM_CMD + '@' + botApiProperties.getUsername())
            ? Optional.of(new MentionAllEvent(chatId))
            : Optional.empty();
    }

    private boolean hasNotType(List<MessageEntity> entityList, String type) {
        return entityList == null
            || entityList.stream()
                .noneMatch(messageEntity -> messageEntity.getType().equals(type));
    }

    private static boolean isInvalidMessage(Message msg) {
        return msg == null || msg.getChat() == null;
    }
}
