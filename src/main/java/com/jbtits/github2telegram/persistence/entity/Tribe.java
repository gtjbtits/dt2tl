package com.jbtits.github2telegram.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "tribes")
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Tribe extends BaseIdEntity {

  @ToString.Include
  private String name;

  @OneToMany(mappedBy = "tribe", orphanRemoval = true)
  private List<Team> teams;
}
