package com.jbtits.github2telegram.domain.dto.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(of = {"name"})
public class TeamRequest {
	
	private String name;
	private Set<FellowRequest> fellows;

}
