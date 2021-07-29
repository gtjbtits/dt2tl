package com.jbtits.github2telegram.service.tlgrm.impl;

import com.jbtits.github2telegram.component.tlgrm.TlgrmSender;
import com.jbtits.github2telegram.service.tlgrm.TlgrmMetaService;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.ChatMember;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
public class TlgrmCachedMetaService implements TlgrmMetaService {

  private final ConcurrentMap<ChatMemberKey, ChatMember> cachedChatMembers = new ConcurrentHashMap<>();

  private final TlgrmSender tlgrmSender;

  @Override
  public ChatMember obtainChatMemberMetadata(final long chatId, final long userId) {
    final ChatMemberKey key = new ChatMemberKey(chatId, userId);
    if (this.cachedChatMembers.containsKey(key)) {
      return this.cachedChatMembers.get(key);
    }
    final ChatMember chatMember = this.tlgrmSender.getChatMemberMetadata(chatId, userId);
    this.cachedChatMembers.put(key, chatMember);
    return chatMember;
  }

  @EqualsAndHashCode
  private static class ChatMemberKey {

    private final long chatId;
    private final long userId;

    private ChatMemberKey(final long chatId, final long userId) {
      this.chatId = chatId;
      this.userId = userId;
    }
  }
}
