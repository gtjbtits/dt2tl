package com.jbtits.github2telegram.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
@EqualsAndHashCode(callSuper = false, of = {"username"})
public class Developer extends BaseIdEntity {

  @Column(name = "username")
  private String username;

  @ManyToOne
  @JoinColumn(name = "team_id")
  private Team team;
}
