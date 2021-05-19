package com.jbtits.github2telegram.helpers.impl;

import com.jbtits.github2telegram.component.tlgrm.TlgrmSender;
import com.jbtits.github2telegram.domain.dto.cfg.TeamConfiguration;
import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmMessageContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.helpers.MessageHelper;
import com.jbtits.github2telegram.helpers.TlgrmMessageHelper;
import com.jbtits.github2telegram.helpers.TlgrmMetaHelper;
import com.jbtits.github2telegram.listener.tlgrm.cbq.TlgrmCallbackQueryAction;
import com.jbtits.github2telegram.listener.tlgrm.cbq.TlgrmCallbackQueryData;
import com.jbtits.github2telegram.persistence.entity.Fellow;
import com.jbtits.github2telegram.persistence.entity.tlgrm.TlgrmUser;
import com.jbtits.github2telegram.service.cfg.ConfigurationPrinterService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.jbtits.github2telegram.util.TlgrmUtils.MARKDOWN_PARSE_MODE;

@Component
@RequiredArgsConstructor
public class DefaultTlgrmMessageHelper implements TlgrmMessageHelper {

  private final MessageHelper messageHelper;
  private final TlgrmSender tlgrmSender;
  private final TlgrmMetaHelper tlgrmMetaHelper;

  private final ConfigurationPrinterService<TlgrmChatContext, TlgrmUserContext> configurationPrinterService;

  @Override
  @NonNull
  public void sendTribeConfigurationMessage(final @NonNull TlgrmChatContext context,
                                            final @NonNull TribeConfiguration<TlgrmChatContext, TlgrmUserContext> configuration) {
    final SendMessage.SendMessageBuilder builder = SendMessage.builder()
        .chatId(String.valueOf(context.getChatId()));
      final List<InlineKeyboardButton> rowWithGoToTeamSelectMsg
          = List.of(this.buildInlineKeyboardButtonFromCode("tlgrm_btn_select_your_team", TlgrmCallbackQueryData.encode(List.of(
          TlgrmCallbackQueryAction.CONFIG_GOTO_JOIN_TEAM_STAGE.getText()))));
      final List<InlineKeyboardButton> rowWithCancelBtn
          = List.of(
          this.buildInlineKeyboardButtonFromCode("tlgrm_btn_cancel_config", TlgrmCallbackQueryData.encode(List.of(
              TlgrmCallbackQueryAction.CONFIG_CANCEL.getText()))),
          this.buildInlineKeyboardButtonFromCode("tlgrm_btn_save_config", TlgrmCallbackQueryData.encode(List.of(
              TlgrmCallbackQueryAction.CONFIG_SAVE.getText()))));
      final List<List<InlineKeyboardButton>> rows = new ArrayList<>();
      if (configuration.getTeams().size() > 1) {
        configuration.getTeams().stream()
            .map(TeamConfiguration::getName)
            .forEach(teamName -> {
              final List<InlineKeyboardButton> rowWithTeamRemoveBtn
                  = List.of(
                  this.buildInlineKeyboardButtonFromLabel(
                      this.messageHelper.getMsg("tlgrm_btn_team_remove_config", teamName),
                      TlgrmCallbackQueryData.encode(List.of(
                          TlgrmCallbackQueryAction.CONFIG_TEAM_REMOVE.getText(), teamName))));
              rows.add(rowWithTeamRemoveBtn);
            });
      }
      if (!configuration.getTeams().isEmpty()) {
        rows.add(rowWithGoToTeamSelectMsg);
      }
      rows.add(rowWithCancelBtn);
      final var inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
          .keyboard(rows)
          .build();
    final String welcomeText = this.messageHelper.getMsg(
        "config_tribe_config_hint",
        this.configurationPrinterService.printTeamsWithFellows(configuration),
        this.messageHelper.getMsg("tlgrm_btn_select_your_team"));
      builder
          .text(welcomeText);
      builder
          .replyMarkup(inlineKeyboardMarkup);
    final var sendMessage = builder.build();
    sendMessage.enableMarkdown(true);
    this.tlgrmSender.safeExecute(sendMessage);
  }

  @Override
  @NonNull
  public void updateTribeConfigurationMessage(final @NonNull TlgrmMessageContext context,
                                              final @NonNull TribeConfiguration<TlgrmChatContext, TlgrmUserContext> configuration) {
    final EditMessageText.EditMessageTextBuilder builder = EditMessageText.builder()
        .messageId((int) context.getMessageId())
        .chatId(String.valueOf(context.getChatId()));
    final List<InlineKeyboardButton> rowWithGoToTeamSelectMsg
        = List.of(this.buildInlineKeyboardButtonFromCode("tlgrm_btn_select_your_team", TlgrmCallbackQueryData.encode(List.of(
        TlgrmCallbackQueryAction.CONFIG_GOTO_JOIN_TEAM_STAGE.getText()))));
    final List<InlineKeyboardButton> rowWithCancelBtn
        = List.of(
          this.buildInlineKeyboardButtonFromCode("tlgrm_btn_cancel_config", TlgrmCallbackQueryData.encode(List.of(
              TlgrmCallbackQueryAction.CONFIG_CANCEL.getText()))),
          this.buildInlineKeyboardButtonFromCode("tlgrm_btn_save_config", TlgrmCallbackQueryData.encode(List.of(
              TlgrmCallbackQueryAction.CONFIG_SAVE.getText()))));
    final List<List<InlineKeyboardButton>> rows = new ArrayList<>();
    if (configuration.getTeams().size() > 1) {
      configuration.getTeams().stream()
          .map(TeamConfiguration::getName)
          .forEach(teamName -> {
            final List<InlineKeyboardButton> rowWithTeamRemoveBtn
                = List.of(
                this.buildInlineKeyboardButtonFromLabel(
                    this.messageHelper.getMsg("tlgrm_btn_team_remove_config", teamName),
                    TlgrmCallbackQueryData.encode(List.of(
                        TlgrmCallbackQueryAction.CONFIG_TEAM_REMOVE.getText(), teamName))));
            rows.add(rowWithTeamRemoveBtn);
          });
    }
    if (!configuration.getTeams().isEmpty()) {
      rows.add(rowWithGoToTeamSelectMsg);
    }
    rows.add(rowWithCancelBtn);
    final var inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
        .keyboard(rows)
        .build();
    final String welcomeText = this.messageHelper.getMsg(
        "config_tribe_config_hint",
        this.configurationPrinterService.printTeamsWithFellows(configuration),
        this.messageHelper.getMsg("tlgrm_btn_select_your_team"));
    builder
        .text(welcomeText);
    builder
        .replyMarkup(inlineKeyboardMarkup);
    final var editMessageText = builder.build();
    editMessageText.enableMarkdown(true);
    this.tlgrmSender.safeExecute(editMessageText);
  }

  @Override
  @NonNull
  public void updateTeamsConfigurationMessage(final @NonNull TlgrmMessageContext context,
                                              final @NonNull TribeConfiguration<TlgrmChatContext, TlgrmUserContext> configuration) {
    final EditMessageText.EditMessageTextBuilder builder = EditMessageText.builder()
        .messageId((int) context.getMessageId())
        .chatId(String.valueOf(context.getChatId()));
    final List<InlineKeyboardButton> rowWithGoBackBtn
        = List.of(
            this.buildInlineKeyboardButtonFromCode("tlgrm_btn_back_to_tribe_configuration", TlgrmCallbackQueryData.encode(List.of(
                TlgrmCallbackQueryAction.CONFIG_BACK.getText()))));
    final List<InlineKeyboardButton> rowWithCancelBtn
        = List.of(
        this.buildInlineKeyboardButtonFromCode("tlgrm_btn_cancel_config", TlgrmCallbackQueryData.encode(List.of(
            TlgrmCallbackQueryAction.CONFIG_CANCEL.getText()))),
        this.buildInlineKeyboardButtonFromCode("tlgrm_btn_save_config", TlgrmCallbackQueryData.encode(List.of(
            TlgrmCallbackQueryAction.CONFIG_SAVE.getText()))));
    final List<List<InlineKeyboardButton>> rows = new ArrayList<>();
    configuration.getTeams().stream()
        .map(TeamConfiguration::getName)
        .forEach(teamName -> {
          final List<InlineKeyboardButton> rowWithTeamRemoveBtn
              = List.of(
              this.buildInlineKeyboardButtonFromLabel(
                  this.messageHelper.getMsg("tlgrm_btn_team_join_config", teamName),
                  TlgrmCallbackQueryData.encode(List.of(
                      TlgrmCallbackQueryAction.CONFIG_TEAM_JOIN.getText(), teamName))));
          rows.add(rowWithTeamRemoveBtn);
        });
    rows.add(rowWithGoBackBtn);
    rows.add(rowWithCancelBtn);
    final var inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
        .keyboard(rows)
        .build();
    final String welcomeText = this.messageHelper.getMsg(
        "config_teams_config_hint",
        this.configurationPrinterService.printTeamsWithFellows(configuration),
        this.messageHelper.getMsg("tlgrm_btn_back_to_tribe_configuration"));
    builder
        .text(welcomeText);
    builder
        .replyMarkup(inlineKeyboardMarkup);
    final var editMessageText = builder.build();
    editMessageText.enableMarkdown(true);
    this.tlgrmSender.safeExecute(editMessageText);
  }

  @Override
  @NonNull
  public void finalizeConfigurationMessage(final @NonNull TlgrmMessageContext context,
                                           final @NonNull TribeConfiguration<TlgrmChatContext, TlgrmUserContext> configuration) {
    final EditMessageText.EditMessageTextBuilder builder = EditMessageText.builder()
        .messageId((int) context.getMessageId())
        .chatId(String.valueOf(context.getChatId()));
    final String welcomeText = this.messageHelper.getMsg(
        "config_saved_hint",
        this.configurationPrinterService.printTeamsWithFellows(configuration));
    builder
        .text(welcomeText);
    final var editMessageText = builder.build();
    editMessageText.enableMarkdown(true);
    this.tlgrmSender.safeExecute(editMessageText);
  }

  @Override
  @NonNull
  public void sendReviewAnnounce(final @NonNull TlgrmChatContext context,
                                 final @NonNull Map<Fellow, TlgrmUser> reviewers,
                                 final @NonNull String prLink) {
    final var chatMembers = reviewers.keySet().stream()
        .map(fellow -> {
          final var tlgrmUser = reviewers.get(fellow);
          return this.tlgrmSender.getChatMemberMetadata(context.getChatId(), tlgrmUser.getTlgrmUserId());
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    final String invites = chatMembers.stream()
        .map(tlgrmMetaHelper::constructUserNameWithUsername)
        .collect(Collectors.joining(", "));
    final SendMessage sendMessage = SendMessage.builder()
        .text(this.messageHelper.getMsg("tlgrm_msg_pr_announce", prLink, invites))
        .chatId(String.valueOf(context.getChatId()))
//        .parseMode(MARKDOWN_PARSE_MODE)
        .build();
    this.tlgrmSender.safeExecute(sendMessage);
  }

  @NonNull
  private InlineKeyboardButton buildInlineKeyboardButtonFromLabel(@NonNull final String label,
                                                                  @NonNull final String data) {
    return InlineKeyboardButton.builder().text(label).callbackData(data).build();
  }

  @NonNull
  private InlineKeyboardButton buildInlineKeyboardButtonFromCode(@NonNull final String code,
                                                                 @NonNull final String data) {
    final var label = this.messageHelper.getMsg(code);
    return InlineKeyboardButton.builder().text(label).callbackData(data).build();
  }

  @Override
  public void sendError(final @NonNull String code, final long chatId, Object ... msgParams) {
    final var sendMessage = SendMessage.builder()
        .text(this.messageHelper.getMsg(code, msgParams))
        .chatId(String.valueOf(chatId))
        .parseMode(MARKDOWN_PARSE_MODE)
        .build();
    this.tlgrmSender.safeExecute(sendMessage);
  }

  @Override
  public void deleteMessage(final long chatId, final long messageId) {
    final var deleteMessage = DeleteMessage.builder()
        .chatId(String.valueOf(chatId))
        .messageId((int) messageId)
        .build();
    this.tlgrmSender.safeExecute(deleteMessage);
  }
}
