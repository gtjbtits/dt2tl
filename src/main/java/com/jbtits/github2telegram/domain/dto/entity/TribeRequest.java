package com.jbtits.github2telegram.domain.dto.entity;

import lombok.Data;

import java.util.Set;

@Data
public class TribeRequest {

	private String name;
	private Set<TeamRequest> teams;
	private String chatId;
}
