package com.jbtits.github2telegram.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(callSuper = false, of = {"name"})
public class Team extends BaseIdEntity {

  @Column(name = "name")
  private String name;

  @OneToMany(mappedBy = "team")
  private Set<Developer> developers;
}
