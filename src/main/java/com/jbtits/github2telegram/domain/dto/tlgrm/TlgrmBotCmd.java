package com.jbtits.github2telegram.domain.dto.tlgrm;

import lombok.Getter;

@Getter
public enum TlgrmBotCmd {
  START("/start"),
  CONFIG("/config");

  private final String cmd;

  TlgrmBotCmd(String cmd) {
    this.cmd = cmd;
  }
}
