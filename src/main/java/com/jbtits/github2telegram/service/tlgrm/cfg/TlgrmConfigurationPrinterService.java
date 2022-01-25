package com.jbtits.github2telegram.service.tlgrm.cfg;

import com.jbtits.github2telegram.component.tlgrm.TlgrmSender;
import com.jbtits.github2telegram.domain.dto.cfg.TeamConfiguration;
import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.helpers.MessageHelper;
import com.jbtits.github2telegram.helpers.TlgrmMetaHelper;
import com.jbtits.github2telegram.service.cfg.ConfigurationPrinterService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TlgrmConfigurationPrinterService
    implements ConfigurationPrinterService<TlgrmChatContext, TlgrmUserContext> {

  private final TlgrmSender tlgrmSender;
  private final TlgrmMetaHelper tlgrmMetaHelper;
  private final MessageHelper messageHelper;

  @Override
  public @NonNull String printTeams(
      final @NonNull TribeConfiguration<TlgrmChatContext, TlgrmUserContext> tribeConfiguration) {
    return tribeConfiguration.getTeams().stream()
        .map(TeamConfiguration::getName)
        .collect(Collectors.toSet()).toString();
  }

  @Override
  public @NonNull String printTeamsWithFellows(
      final @NonNull TribeConfiguration<TlgrmChatContext, TlgrmUserContext> tribeConfiguration) {
    final var builder = new StringBuilder();
    tribeConfiguration.getTeams().forEach(teamConfiguration -> {
      builder.append(teamConfiguration.getName()).append(":\n");
      if (teamConfiguration.getFellows().isEmpty()) {
        builder
            .append("  ")
            .append(this.messageHelper.getMsg("tlgrm_empty_team"))
            .append("\n");
        return;
      }
      teamConfiguration.getFellows().forEach(fellowConfiguration -> {
        final TlgrmUserContext userContext = fellowConfiguration.getContext();
        final ChatMember chatMember =
            this.tlgrmSender.getChatMemberMetadata(userContext.getChatId(), userContext.getUserId());
        if (chatMember == null) {
          throw new IllegalStateException("Can't find ChatMember for user");
        }
        builder
            .append("  ")
            .append(this.tlgrmMetaHelper.constructUserNameWithUsername(chatMember))
            .append("\n");
      });
    });
    return builder.toString();
  }
}
