package com.jbtits.github2telegram.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.ZonedDateTime;

@Data
@MappedSuperclass
@EqualsAndHashCode(exclude = {"created", "updated"})
abstract class BaseIdEntity {
  @Id
  private long id;

  @CreationTimestamp
  private ZonedDateTime created;

  @UpdateTimestamp
  private ZonedDateTime updated;
}
