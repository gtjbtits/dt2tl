package com.jbtits.github2telegram.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "fellows")
@EqualsAndHashCode(callSuper = true)
public class Fellow extends BaseBusinessEntity {

  private String name;

  @ManyToOne
  @JoinColumn(name = "team_id")
  private Team team;
}
