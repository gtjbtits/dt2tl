package com.jbtits.github2telegram.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.jbtits.github2telegram.domain.dto.entity.TribeRequest;
import com.jbtits.github2telegram.domain.dto.entity.TribeResponse;
import com.jbtits.github2telegram.service.impl.DefaultConfigurationService;

class ConfigurationServiceTest {
	
	@Test
	void build_Must_ReturnEmptyConfiguration_IfTribeRequestIsEmptyAndNoTribeInfoHasBeenPersistedBefore() {
		final ConfigurationService configurationService = new DefaultConfigurationService();
		final TribeRequest newConfiguration = new TribeRequest();
		final TribeResponse persistedConfiguration = new TribeResponse();
		final var buildedConfig = configurationService.build(newConfiguration, persistedConfiguration);
		assertNotNull(buildedConfig);
		assertTrue(buildedConfig.isEmpty());
	}
}
