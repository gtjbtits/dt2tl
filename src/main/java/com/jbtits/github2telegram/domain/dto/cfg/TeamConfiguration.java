package com.jbtits.github2telegram.domain.dto.cfg;

import com.jbtits.github2telegram.domain.dto.common.AbstractContext;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@EqualsAndHashCode(callSuper = true)
public class TeamConfiguration<C extends AbstractContext> extends ConfigurationPart<C> {

	@NonNull
	private final String name;

	private final Set<FellowConfiguration<C>> fellows = new HashSet<>();

	public TeamConfiguration(@NotNull final C context, @NonNull final String name) {
		super(context);
		this.name = name;
	}
}
