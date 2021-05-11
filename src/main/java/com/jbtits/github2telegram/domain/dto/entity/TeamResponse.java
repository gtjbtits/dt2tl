package com.jbtits.github2telegram.domain.dto.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class TeamResponse extends TeamRequest {
	private long id;
	
	public TeamResponse(long id, @NonNull String name) {
		this.id = id;
	}
}
