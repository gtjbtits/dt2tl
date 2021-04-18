package com.jbtits.github2telegram.domain.dto.entity;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(of = {"name"})
public class TeamRequest {
	
	@NonNull
	private String name;
	
	@NonNull
	private final Set<FellowRequest> fellows = new HashSet<>();

}
