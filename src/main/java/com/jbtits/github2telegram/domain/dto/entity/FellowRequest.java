package com.jbtits.github2telegram.domain.dto.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"telegramUserId"})
public class FellowRequest {
	
	private String name;
	
	private String telegramUserId;
}
