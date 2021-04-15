package com.jbtits.github2telegram.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tlgrm_chats")
@EqualsAndHashCode(callSuper = true)
public class TelegramChat extends BaseBusinessEntity {
  @Column(name = "tlgrm_id")
  private long telegramId;

  @OneToOne(mappedBy = "telegramChat")
  private Tribe tribe;
}
