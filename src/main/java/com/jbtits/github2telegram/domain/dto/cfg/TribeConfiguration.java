package com.jbtits.github2telegram.domain.dto.cfg;

import com.jbtits.github2telegram.domain.dto.context.AbstractChatContext;
import com.jbtits.github2telegram.domain.dto.context.AbstractUserContext;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@EqualsAndHashCode(callSuper = true)
public class TribeConfiguration<C extends AbstractChatContext, U extends AbstractUserContext> extends
		ConfigurationWithContext<C> {

	@NonNull
	private final String name;

	private final Set<TeamConfiguration<U>> teams = new HashSet<>();

	public TribeConfiguration(@NotNull final C context, @NonNull final String name) {
		super(context);
		this.name = name;
	}
}