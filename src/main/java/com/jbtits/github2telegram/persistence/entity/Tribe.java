package com.jbtits.github2telegram.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Data
@Entity
@Table(name = "tribes")
@EqualsAndHashCode(callSuper = true)
public class Tribe extends BaseBusinessEntity {

  private String name;

  @OneToMany(mappedBy = "tribe")
  private Set<Team> teams;
}
