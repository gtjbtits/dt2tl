package com.jbtits.github2telegram.listener.tlgrm.cbq;

public abstract class TlgrmCallbackQueryData {

  private TlgrmCallbackQueryData() {

  }

  private static final String CONFIG_PREFIX = "cfg_";
  public static final String CONFIG_JOIN = CONFIG_PREFIX + "join";
  public static final String CONFIG_SAVE = CONFIG_PREFIX + "save";
  public static final String CONFIG_CANCEL = CONFIG_PREFIX + "cancel";
  public static final String CONFIG_RESET = CONFIG_PREFIX + "reset";
}
