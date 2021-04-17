package com.jbtits.github2telegram.domain.dto.entity;

import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"name"})
public class TeamRequest {
	
	private String name;
	
	private Set<FellowRequest> fellows;

}
