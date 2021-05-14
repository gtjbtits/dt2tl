package com.jbtits.github2telegram.service;

import com.jbtits.github2telegram.service.impl.TlgrmConfigurationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TlgrmConfigurationServiceTest {
	
	private static ConfigurationService configurationService;
	
	@BeforeAll
	static void setUp() {
		configurationService = new TlgrmConfigurationService();
	}

	@Test
	void build_Must_buildCorrectConfigForCorrectFile() {

	}
}
