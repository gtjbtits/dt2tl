package com.jbtits.github2telegram.service.impl;

import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.entity.TribeRequest;
import com.jbtits.github2telegram.domain.dto.entity.TribeResponse;
import com.jbtits.github2telegram.service.ConfigurationService;

public class DefaultConfigurationService implements ConfigurationService {

	@Override
	public TribeConfiguration build(TribeRequest newConfig, TribeResponse persistendConfig) {
		return new TribeConfiguration();
	}

	@Override
	public void apply(TribeConfiguration config) {
	}

}
