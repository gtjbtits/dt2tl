package com.jbtits.github2telegram.domain.dto.cfg;

import java.util.HashSet;
import java.util.Set;

import com.jbtits.github2telegram.domain.dto.entity.TeamRequest;
import com.jbtits.github2telegram.domain.dto.entity.TeamResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class TeamConfiguration extends ConfigurationPart<TeamRequest, TeamResponse> {

	private final FellowConfiguration fellowConfiguration = new FellowConfiguration();

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && fellowConfiguration.isEmpty();
	}
	
	
}
