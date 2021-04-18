package com.jbtits.github2telegram.domain.dto.entity;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(of = {"telegramChatId"})
public class TribeRequest {

	@NonNull
	private String name;
	
	@NonNull
	private final Set<TeamRequest> teams = new HashSet<>();
	
	@NonNull
	private String telegramChatId;
}
