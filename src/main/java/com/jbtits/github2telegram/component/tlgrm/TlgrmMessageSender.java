package com.jbtits.github2telegram.component.tlgrm;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import lombok.NonNull;

public interface TlgrmMessageSender {

  void sendMessage(@NonNull String text, @NonNull TlgrmChatContext context);
}
