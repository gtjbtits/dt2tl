package com.jbtits.github2telegram.service.tlgrm;

import org.telegram.telegrambots.meta.api.objects.ChatMember;

public interface TlgrmMetaService {

  ChatMember obtainChatMemberMetadata(long chatId, long userId);

}
