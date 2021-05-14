package com.jbtits.github2telegram.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Data
@Entity
@Table(name = "teams")
@EqualsAndHashCode(callSuper = true)
public class Team extends BaseBusinessEntity {

  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tribe_id")
  private Tribe tribe;

  @OneToMany(mappedBy = "team")
  private Set<Fellow> fellows;
}
