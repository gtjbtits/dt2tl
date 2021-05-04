package com.jbtits.github2telegram.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseIdEntity {
  @Id
  private long id;

}
