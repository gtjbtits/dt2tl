package com.jbtits.github2telegram.domain.dto.cfg;

import com.jbtits.github2telegram.domain.dto.context.AbstractContext;
import lombok.Data;

@Data
public abstract class ConfigurationWithContext<C extends AbstractContext> {

	private final C context;
}
