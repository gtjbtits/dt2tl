package com.jbtits.github2telegram.persistence.repository.tlgrm;

import com.jbtits.github2telegram.persistence.entity.tlgrm.TlgrmChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TlgrmChatRepository extends JpaRepository<TlgrmChat, Long> {

  Optional<TlgrmChat> findByTlgrmChatId(long chatId);
}
