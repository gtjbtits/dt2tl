package com.jbtits.github2telegram.domain.dto.tlgrm.cfg.mapping;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(of = {"name"})
public class TlgrmTeam {

	private String name;
	private Set<TlgrmFellow> fellows;
}
