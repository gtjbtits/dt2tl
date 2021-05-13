package com.jbtits.github2telegram.service;

import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.common.AbstractContext;
import com.jbtits.github2telegram.domain.dto.entity.TribeRequest;
import lombok.NonNull;

import java.io.File;

public interface ConfigurationService<C extends AbstractContext> {
	
	@NonNull
	TribeConfiguration<C> build(@NonNull File file, @NonNull C context);

	@NonNull
	TribeConfiguration<C> build(@NonNull TribeRequest request, @NonNull C context);

	@NonNull
	String toString(@NonNull TribeConfiguration<C> configuration);

	void apply(@NonNull TribeConfiguration<C> configuration);
}
