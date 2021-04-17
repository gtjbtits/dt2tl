package com.jbtits.github2telegram.domain.dto.cfg;

import java.util.HashSet;
import java.util.Set;

import com.jbtits.github2telegram.domain.dto.entity.TribeRequest;
import com.jbtits.github2telegram.domain.dto.entity.TribeResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class TribeConfiguration extends ConfigurationPart<TribeRequest, TribeResponse> {
	
	private Set<TeamConfiguration> teamConfigurations = new HashSet<>();

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && teamConfigurations.isEmpty();
	}
	
	
}
