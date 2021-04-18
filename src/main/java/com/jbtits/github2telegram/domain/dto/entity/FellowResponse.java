package com.jbtits.github2telegram.domain.dto.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class FellowResponse extends FellowRequest {
	private long id;
	
	public FellowResponse(long id, @NonNull String name, @NonNull String telegramUserId) {
		super(name, telegramUserId);
		this.id = id;
	}
}
