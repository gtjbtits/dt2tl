package com.jbtits.github2telegram.persistence.service;

import com.jbtits.github2telegram.domain.dto.cfg.TeamConfiguration;
import com.jbtits.github2telegram.domain.dto.context.AbstractUserContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.persistence.entity.Team;
import com.jbtits.github2telegram.persistence.entity.Tribe;
import lombok.NonNull;

import java.util.List;

public interface TeamService<U extends AbstractUserContext> {

  void create(@NonNull List<TeamConfiguration<U>> teamConfigurations, @NonNull Tribe tribe);

  void drop(final @NonNull TlgrmChatContext tlgrmChatContext);

  List<Team> findAll();
}
