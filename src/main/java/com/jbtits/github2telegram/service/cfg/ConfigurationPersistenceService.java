package com.jbtits.github2telegram.service.cfg;

import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.context.AbstractChatContext;
import com.jbtits.github2telegram.domain.dto.context.AbstractUserContext;
import lombok.NonNull;

import java.util.Optional;

public interface ConfigurationPersistenceService<C extends AbstractChatContext, U extends AbstractUserContext> {

	Optional<TribeConfiguration<C,U>> get(@NonNull C context);

	void save(@NonNull TribeConfiguration<C, U> configuration);

	void deactivate(@NonNull final C context);

	void activate(@NonNull final C context);
}
