package com.jbtits.github2telegram.listener.tlgrm.cbq;

import lombok.Getter;
import lombok.NonNull;

@Getter
public enum TlgrmCallbackQueryAction {
  CONFIG_GOTO_JOIN_TEAM_STAGE("cfg_goto_join_st"),
  CONFIG_SAVE("cfg_save"),
  CONFIG_CANCEL("cfg_cancel"),
  CONFIG_BACK("cfg_back"),
  CONFIG_TEAM_REMOVE("cfg_team_rm"),
  CONFIG_TEAM_ADD("cfg_team_add"),
  CONFIG_TEAM_JOIN("cfg_team_join");

  @NonNull
  private final String text;

  TlgrmCallbackQueryAction(@NonNull final String text) {
    this.text = text;
  }

  @NonNull
  public static TlgrmCallbackQueryAction fromString(@NonNull final String str) {
    switch (str) {
      case "cfg_goto_join_st":
        return CONFIG_GOTO_JOIN_TEAM_STAGE;
      case "cfg_save":
        return CONFIG_SAVE;
      case "cfg_cancel":
        return CONFIG_CANCEL;
      case "cfg_team_rm":
        return CONFIG_TEAM_REMOVE;
      case "cfg_team_add":
        return CONFIG_TEAM_ADD;
      case "cfg_team_join":
        return CONFIG_TEAM_JOIN;
      case "cfg_back":
        return CONFIG_BACK;
      default:
        throw new IllegalArgumentException("Can't construct action object from string " + str);
    }
  }

}
