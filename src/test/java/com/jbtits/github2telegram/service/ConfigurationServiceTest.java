package com.jbtits.github2telegram.service;

import com.jbtits.github2telegram.service.impl.TlgrmConfigurationService;
import org.junit.jupiter.api.BeforeAll;

class ConfigurationServiceTest {
	
	private static ConfigurationService configurationService;
	
	@BeforeAll
	static void setUp() {
		configurationService = new TlgrmConfigurationService();
	}
}
