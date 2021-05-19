package com.jbtits.github2telegram.domain.dto.cfg;

import com.jbtits.github2telegram.domain.dto.context.AbstractUserContext;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TeamConfiguration<U extends AbstractUserContext> {

	public static final String DEFAULT_TEAM_NAME = "DevTeam";

	@NonNull
	@EqualsAndHashCode.Include
	private final String name;

	@EqualsAndHashCode.Include
	private final List<FellowConfiguration<U>> fellows = new ArrayList<>();

	public TeamConfiguration(@NonNull final String name) {
		this.name = name;
	}
}
