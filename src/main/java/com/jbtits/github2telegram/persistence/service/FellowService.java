package com.jbtits.github2telegram.persistence.service;

import com.jbtits.github2telegram.domain.dto.cfg.FellowConfiguration;
import com.jbtits.github2telegram.domain.dto.context.AbstractUserContext;
import com.jbtits.github2telegram.persistence.entity.Team;
import lombok.NonNull;

import java.util.List;

public interface FellowService<U extends AbstractUserContext> {

  void save(@NonNull final List<FellowConfiguration<U>> fellowConfigurations,
            @NonNull final Team team);
}
