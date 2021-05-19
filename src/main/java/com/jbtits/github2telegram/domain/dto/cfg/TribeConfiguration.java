package com.jbtits.github2telegram.domain.dto.cfg;

import com.jbtits.github2telegram.domain.dto.context.AbstractChatContext;
import com.jbtits.github2telegram.domain.dto.context.AbstractUserContext;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class TribeConfiguration<C extends AbstractChatContext, U extends AbstractUserContext> extends
		ConfigurationWithContext<C> {

	public static final String DEFAULT_TRIBE_NAME = "DevTribe";

	@NonNull
	@EqualsAndHashCode.Include
	private final String name;

	@EqualsAndHashCode.Include
	private final List<TeamConfiguration<U>> teams = new ArrayList<>();

	public TribeConfiguration(@NotNull final C context, @NonNull final String name) {
		super(context);
		this.name = name;
	}
}
