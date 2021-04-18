package com.jbtits.github2telegram.domain.dto.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class TribeResponse extends TribeRequest {
	private final long id;
	
	public TribeResponse(long id, @NonNull String name, @NonNull String telegramChatId) {
		super(name, telegramChatId);
		this.id = id;
	}
}
