package com.jbtits.github2telegram.domain.dto.cfg;

import com.jbtits.github2telegram.domain.dto.common.AbstractContext;
import lombok.Data;

@Data
public abstract class ConfigurationPart<C extends AbstractContext> {

	private final C context;
}
