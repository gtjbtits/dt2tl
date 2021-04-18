package com.jbtits.github2telegram.service;

import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.entity.TribeRequest;
import com.jbtits.github2telegram.domain.dto.entity.TribeResponse;

public interface ConfigurationService {
	
	TribeConfiguration build(TribeRequest request, TribeResponse persisted);
	
	TribeConfiguration build(TribeRequest request);
	
	void apply(TribeConfiguration config);
}
