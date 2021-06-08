package com.jbtits.github2telegram.domain.event.tlgrm.msg.reply;

import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmMessageContext;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class AddTeamConfigurationReplyEvent extends AbstractTlgrmReplyEvent<TlgrmMessageContext> {

  @NonNull
  @EqualsAndHashCode.Include
  private final String teamName;

  public AddTeamConfigurationReplyEvent(final @NonNull TlgrmMessageContext context,
                                        final @NonNull String teamName) {
    super(context);
    this.teamName = teamName;
  }
}
