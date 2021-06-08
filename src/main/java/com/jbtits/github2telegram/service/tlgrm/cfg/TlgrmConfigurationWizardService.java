package com.jbtits.github2telegram.service.tlgrm.cfg;

import com.jbtits.github2telegram.component.tlgrm.TlgrmSender;
import com.jbtits.github2telegram.domain.dto.cfg.FellowConfiguration;
import com.jbtits.github2telegram.domain.dto.cfg.TeamConfiguration;
import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.domain.exception.tlgrm.TlgrmMetadataObtainingException;
import com.jbtits.github2telegram.helpers.TlgrmMetaHelper;
import com.jbtits.github2telegram.service.cfg.ConfigurationWizardService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.util.Objects;
import java.util.stream.Collectors;

import static com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration.DEFAULT_TRIBE_NAME;

@Service
@RequiredArgsConstructor
public class TlgrmConfigurationWizardService
    implements ConfigurationWizardService<TlgrmChatContext, TlgrmUserContext> {

  private final TlgrmSender tlgrmSender;
  private final TlgrmMetaHelper tlgrmMetaHelper;

  @Override
  public @NonNull TribeConfiguration<TlgrmChatContext, TlgrmUserContext> generateEmptyConfiguration(
      @NonNull TlgrmChatContext context) {
    final TribeConfiguration<TlgrmChatContext, TlgrmUserContext> tribeConfiguration
        = new TribeConfiguration<>(context, this.determineTribeName(context));
    final TeamConfiguration<TlgrmUserContext> teamConfiguration
        = new TeamConfiguration<>(TeamConfiguration.DEFAULT_TEAM_NAME);
    tribeConfiguration.getTeams().add(teamConfiguration);
    return tribeConfiguration;
  }

  @Override
  public boolean isTeamNameUnique(final @NonNull String teamName,
                                  final @NonNull TribeConfiguration<TlgrmChatContext, TlgrmUserContext> tribeConfiguration) {
    final var teamNames = tribeConfiguration.getTeams().stream()
        .map(TeamConfiguration::getName)
        .collect(Collectors.toSet());
    return !teamNames.contains(teamName);
  }

  @Override
  public void addTeam(final @NonNull String teamName,
                      final @NonNull TribeConfiguration<TlgrmChatContext, TlgrmUserContext> tribeConfiguration) {

    final var teamConfiguration = new TeamConfiguration<TlgrmUserContext>(teamName);
    tribeConfiguration.getTeams().add(teamConfiguration);
  }

  @Override
  public void removeTeam(final @NonNull String teamName,
                         final @NonNull TribeConfiguration<TlgrmChatContext, TlgrmUserContext> tribeConfiguration) {
    tribeConfiguration.getTeams().removeIf(teamConfiguration -> teamConfiguration.getName().equals(teamName));
  }

  @Override
  public boolean changeTeam(final @NonNull String teamName,
                         final @NonNull TribeConfiguration<TlgrmChatContext, TlgrmUserContext> tribeConfiguration,
                         final @NonNull TlgrmUserContext userContext) {
    final var teams = tribeConfiguration.getTeams();
    if (teams == null) {
      throw new IllegalStateException("Tribe teams is null");
    }
    final var oldTeam = teams.stream()
        .filter(teamConfiguration -> this.isTeamContainsUser(teamConfiguration, userContext))
        .findAny();
    if (oldTeam.isPresent() && oldTeam.get().getName().equals(teamName)) {
      return false;
    }
    oldTeam.ifPresent(teamConfiguration -> teamConfiguration.getFellows()
        .removeIf(fellowConfiguration -> Objects.equals(fellowConfiguration.getContext().getUserId(),
            userContext.getUserId())));
    teams.stream()
        .filter(teamConfiguration -> teamConfiguration.getName().equals(teamName))
        .findAny().ifPresent(targetTeamConfiguration -> {
          final var chatMember =
              this.tlgrmSender.getChatMemberMetadata(userContext.getChatId(), userContext.getUserId());
          if (chatMember == null) {
            throw new IllegalStateException("Can't find chat member with user id " + userContext.getUserId());
          }
          final var name = this.tlgrmMetaHelper.extractName(chatMember);
          final var fellowConfiguration = new FellowConfiguration<>(userContext, name);
          targetTeamConfiguration.getFellows().add(fellowConfiguration);
    });
    return true;
  }

  private boolean isTeamContainsUser(final @NonNull TeamConfiguration<TlgrmUserContext> teamConfiguration,
                                     final @NonNull TlgrmUserContext userContext) {
    final var fellows = teamConfiguration.getFellows();
    if (fellows == null || fellows.isEmpty()) {
      return false;
    }
    return fellows.stream()
        .map(FellowConfiguration::getContext)
        .map(TlgrmUserContext::getUserId)
        .anyMatch(userId -> Objects.equals(userId, userContext.getUserId()));
  }

  @NonNull
  private String determineTribeName(@NonNull TlgrmChatContext context) {
    final Chat chat = this.tlgrmSender.getChatMetadata(context.getChatId());
    if (chat == null) {
      throw new TlgrmMetadataObtainingException("Chat is null");
    }
    final String title = chat.getTitle();
    return title == null || title.isBlank()
        ? DEFAULT_TRIBE_NAME
        : title;
  }
}
