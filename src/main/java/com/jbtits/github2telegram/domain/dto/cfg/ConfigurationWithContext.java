package com.jbtits.github2telegram.domain.dto.cfg;

import com.jbtits.github2telegram.domain.dto.context.AbstractContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class ConfigurationWithContext<C extends AbstractContext> {

	@NonNull
	@EqualsAndHashCode.Include
	private final C context;
}
