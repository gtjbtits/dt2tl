package com.jbtits.github2telegram.service.impl;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import com.jbtits.github2telegram.domain.dto.cfg.FellowConfiguration;
import com.jbtits.github2telegram.domain.dto.cfg.TeamConfiguration;
import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.entity.FellowRequest;
import com.jbtits.github2telegram.domain.dto.entity.TeamRequest;
import com.jbtits.github2telegram.domain.dto.entity.TribeRequest;
import com.jbtits.github2telegram.domain.dto.entity.TribeResponse;
import com.jbtits.github2telegram.service.ConfigurationService;

public class DefaultConfigurationService implements ConfigurationService {

	@Override
	public TribeConfiguration build(TribeRequest tribeRequest, TribeResponse tribeResponse) {
		return null;
	}
	
	public TribeConfiguration build(TribeRequest tribeRequest) {
		final TribeConfiguration config = new TribeConfiguration();
		config.getCreating().add(tribeRequest);
		final var teamConfiguration = config.getTeamConfiguration();
		final var teamRequests = tribeRequest.getTeams();
		teamConfiguration.getCreating().addAll(teamRequests);
		final var fellowConfiguration = teamConfiguration.getFellowConfiguration();
		final Set<FellowRequest> fellowRequests = teamRequests.stream()
			.flatMap(tr -> tr.getFellows().stream())
			.collect(Collectors.toSet());
		fellowConfiguration.getCreating().addAll(fellowRequests);
		return config;
	}

	@Override
	public void apply(TribeConfiguration config) {
	}
}
