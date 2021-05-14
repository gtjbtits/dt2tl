package com.jbtits.github2telegram.domain.dto.tlgrm.cfg.mapping;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"username"})
public class TlgrmFellow {

	private String username;
}
