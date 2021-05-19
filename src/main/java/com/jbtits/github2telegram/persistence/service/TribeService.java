package com.jbtits.github2telegram.persistence.service;

import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.context.AbstractChatContext;
import com.jbtits.github2telegram.domain.dto.context.AbstractUserContext;
import com.jbtits.github2telegram.persistence.entity.Fellow;
import com.jbtits.github2telegram.persistence.entity.Tribe;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface TribeService<C extends AbstractChatContext, U extends AbstractUserContext> {

  Optional<Tribe> findById(final long id);

  void save(final @NonNull TribeConfiguration<C, U> configuration);

  void activate(@NonNull final Tribe tribe);

  void deactivate(@NonNull final Tribe tribe);

  @NonNull
  List<Fellow> findReviewers(@NonNull final U userContext);
}
