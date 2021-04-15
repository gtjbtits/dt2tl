package com.jbtits.github2telegram.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Data
@Entity
@Table(name = "tlgrm_users")
@EqualsAndHashCode(callSuper = true)
public class TelegramUser extends BaseBusinessEntity {
  @Column(name = "tlgrm_id")
  private long telegramId;

  @OneToMany(mappedBy = "telegramUser")
  private Set<Fellow> fellows;
}
