package com.jbtits.github2telegram.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
abstract class BaseBusinessEntity extends BaseIdEntity {

  private ZonedDateTime deleted;

  static <T extends BaseBusinessEntity> void markAsDeleted(@NonNull T entity) {
    if (entity.getDeleted() == null) {
      entity.setDeleted(ZonedDateTime.now());
    }
  }
}
