package com.jbtits.github2telegram.domain.dto.tlgrm.cfg.yml;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"username"})
public class TlgrmFellow {

	/**
	 * Telegram username without '@' sign, cause it's special char in .yaml-file
	 */
	private String username;
}
