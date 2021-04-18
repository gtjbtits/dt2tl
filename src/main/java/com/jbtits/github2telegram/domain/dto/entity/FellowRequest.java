package com.jbtits.github2telegram.domain.dto.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(of = {"telegramUserId"})
public class FellowRequest {
	
	@NonNull
	private String name;
	
	@NonNull
	private String telegramUserId;
}
