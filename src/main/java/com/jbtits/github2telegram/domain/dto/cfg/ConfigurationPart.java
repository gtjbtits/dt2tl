package com.jbtits.github2telegram.domain.dto.cfg;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class ConfigurationPart<R,P extends R> {
	
	private final Set<R> creating = new HashSet<>();

	private final Set<UpdatingPair> updating = new HashSet<>();
	
	private final Set<P> deleting = new HashSet<>();
	
	public void putUpdating(@NonNull R toBe, @NonNull P asIs) {
		updating.add(new UpdatingPair(toBe, asIs));
	}
	
	public boolean isEmpty() {
		return creating.isEmpty() && updating.isEmpty() && deleting.isEmpty();
	}
	
	@Data
	@RequiredArgsConstructor
	public class UpdatingPair {
		@NonNull
		private final R toBe;	
		@NonNull
		private final P asIs;
	}
}
