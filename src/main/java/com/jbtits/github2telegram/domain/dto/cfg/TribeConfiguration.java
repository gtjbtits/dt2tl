package com.jbtits.github2telegram.domain.dto.cfg;

import com.jbtits.github2telegram.domain.dto.entity.TribeRequest;
import com.jbtits.github2telegram.domain.dto.entity.TribeResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class TribeConfiguration<C> extends ConfigurationPart<TribeRequest, TribeResponse> {
	
	private final TeamConfiguration teamConfiguration = new TeamConfiguration();

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && teamConfiguration.isEmpty();
	}
	
	
}
