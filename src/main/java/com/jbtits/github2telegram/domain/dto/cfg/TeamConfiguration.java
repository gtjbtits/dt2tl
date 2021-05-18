package com.jbtits.github2telegram.domain.dto.cfg;

import com.jbtits.github2telegram.domain.dto.context.AbstractUserContext;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

@Getter
public class TeamConfiguration<U extends AbstractUserContext> {

	@NonNull
	private final String name;

	private final Set<FellowConfiguration<U>> fellows = new HashSet<>();

	public TeamConfiguration(@NonNull final String name) {
		this.name = name;
	}
}
