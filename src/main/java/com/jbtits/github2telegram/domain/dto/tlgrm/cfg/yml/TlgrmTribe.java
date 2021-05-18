package com.jbtits.github2telegram.domain.dto.tlgrm.cfg.yml;

import lombok.Data;

import java.util.Set;

@Data
public class TlgrmTribe {

	private Set<TlgrmTeam> teams;
}
