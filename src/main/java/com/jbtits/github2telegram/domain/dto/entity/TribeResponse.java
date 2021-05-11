package com.jbtits.github2telegram.domain.dto.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class TribeResponse extends TribeRequest {
	private long id;
}
