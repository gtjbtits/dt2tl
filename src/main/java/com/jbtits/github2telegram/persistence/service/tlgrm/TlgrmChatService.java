package com.jbtits.github2telegram.persistence.service.tlgrm;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.persistence.entity.tlgrm.TlgrmChat;
import com.jbtits.github2telegram.persistence.repository.tlgrm.TlgrmChatRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TlgrmChatService {

  private final TlgrmChatRepository repository;

  @Transactional(readOnly = true)
  public Optional<TlgrmChat> findByTlgrmChatId(long chatId) {
    return this.repository.findByTlgrmChatId(chatId);
  }

  public void save(@NonNull final TlgrmChat tlgrmChat) {
    this.repository.save(tlgrmChat);
  }

  @Transactional(readOnly = true)
  public List<TlgrmChat> findAll() {
    return repository.findAll();
  }

  @NonNull
  public TlgrmChat generate(@NonNull final TlgrmChatContext context) {
    final TlgrmChat tlgrmChat = new TlgrmChat();
    tlgrmChat.setTlgrmChatId(context.getChatId());
    return tlgrmChat;
  }
}
