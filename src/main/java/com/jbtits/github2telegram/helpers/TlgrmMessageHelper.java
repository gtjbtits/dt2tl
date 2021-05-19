package com.jbtits.github2telegram.helpers;

import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmCallbackContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmMessageContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmListenerException;
import com.jbtits.github2telegram.persistence.entity.Fellow;
import com.jbtits.github2telegram.persistence.entity.tlgrm.TlgrmUser;
import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.ChatMember;

import java.util.Map;

public interface TlgrmMessageHelper {

  void sendTribeConfigurationMessage(@NonNull final TlgrmChatContext context,
                                            @NonNull final TribeConfiguration<TlgrmChatContext, TlgrmUserContext> configuration);

  void updateTribeConfigurationMessage(@NonNull final TlgrmMessageContext context,
                                                  @NonNull final TribeConfiguration<TlgrmChatContext, TlgrmUserContext> configuration);

  void updateTeamsConfigurationMessage(@NonNull final TlgrmMessageContext context,
                                       @NonNull final TribeConfiguration<TlgrmChatContext, TlgrmUserContext> configuration);

  void finalizeConfigurationMessage(@NonNull final TlgrmMessageContext context,
                                    @NonNull final TribeConfiguration<TlgrmChatContext, TlgrmUserContext> configuration);

  void sendReviewAnnounce(@NonNull final TlgrmChatContext context,
                          @NonNull final Map<Fellow, TlgrmUser> fellowTlgrmUserMap,
                          @NonNull final String prLink);

  void sendError(@NonNull final String code, long chatId, Object ... msgParams);

  void deleteMessage(long chatId, long messageId);
}
