package com.jbtits.github2telegram.service;

import com.jbtits.github2telegram.domain.dto.cfg.TribeConfiguration;
import com.jbtits.github2telegram.domain.dto.context.AbstractChatContext;
import com.jbtits.github2telegram.domain.dto.context.AbstractUserContext;
import com.jbtits.github2telegram.domain.dto.tlgrm.cfg.yml.TlgrmTribe;
import lombok.NonNull;

import java.io.File;

public interface ConfigurationService<C extends AbstractChatContext, U extends AbstractUserContext> {
	
	@NonNull
	TribeConfiguration<C, U> build(@NonNull File file, @NonNull C context);

	@NonNull
	TribeConfiguration<C, U> build(@NonNull TlgrmTribe request, @NonNull C context);

	@NonNull
	String toString(@NonNull TribeConfiguration<C, U> configuration);

	void apply(@NonNull TribeConfiguration<C, U> configuration);
}
