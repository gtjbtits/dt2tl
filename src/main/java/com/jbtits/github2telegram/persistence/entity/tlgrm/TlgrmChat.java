package com.jbtits.github2telegram.persistence.entity.tlgrm;

import com.jbtits.github2telegram.persistence.entity.BaseIdEntity;
import com.jbtits.github2telegram.persistence.entity.Tribe;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tlgrm_chats")
@EqualsAndHashCode(callSuper = true)
public class TlgrmChat extends BaseIdEntity {
  @Column(name = "tlgrm_id")
  private long telegramId;

  @OneToOne
  @JoinColumn(name = "tribe_id")
  private Tribe tribe;
}
