package com.jbtits.github2telegram.service;

import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmChatContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.TlgrmUserContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.cfg.yml.TlgrmFellow;
import com.jbtits.github2telegram.domain.dto.tlgrm.cfg.yml.TlgrmTeam;
import com.jbtits.github2telegram.domain.dto.tlgrm.cfg.yml.TlgrmTribe;
import com.jbtits.github2telegram.service.impl.TlgrmConfigurationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class TlgrmConfigurationServiceTest {

	public static final long ID_1L = 1L;
	public static final String THOR_USERNAME = "thor";
	private static ConfigurationService<TlgrmChatContext, TlgrmUserContext> configurationService;
	
	@BeforeAll
	static void setUp() {
		configurationService = new TlgrmConfigurationService();
	}

	@Test
	void build_Must_buildCorrectConfigForCorrectFile() {
		final TribeConfiguration<TlgrmChatContext, TlgrmUserContext> configuration
				= configurationService.build(buildTlgrmCfg(), buildTlgrmContext());

	}

	private TlgrmTribe buildTlgrmCfg() {
		final TlgrmTribe tlgrmTribe = new TlgrmTribe();
		final Set<TlgrmTeam> tlgrmTeamSet = new HashSet<>();
		tlgrmTribe.setTeams(tlgrmTeamSet);

		final TlgrmTeam tlgrmTeam = new TlgrmTeam();
		final Set<TlgrmFellow> tlgrmFellowSet = new HashSet<>();
		tlgrmTeam.setName("asgard");
		tlgrmTeam.setFellows(tlgrmFellowSet);

		final TlgrmFellow tlgrmFellow = new TlgrmFellow();
		tlgrmFellow.setUsername(THOR_USERNAME);

		tlgrmTeam.getFellows().add(tlgrmFellow);
		tlgrmTribe.getTeams().add(tlgrmTeam);

		return tlgrmTribe;
	}

	private TlgrmChatContext buildTlgrmContext() {
		return new TlgrmChatContext(ID_1L);
	}
}
