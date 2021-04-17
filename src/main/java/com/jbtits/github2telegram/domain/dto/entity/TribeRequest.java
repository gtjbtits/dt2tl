package com.jbtits.github2telegram.domain.dto.entity;

import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"telegramChatId"})
public class TribeRequest {

	private String name;
	
	private Set<TeamRequest> teams;
	
	private String telegramChatId;
}
