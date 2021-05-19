package com.jbtits.github2telegram.persistence.entity.tlgrm;

import com.jbtits.github2telegram.persistence.entity.BaseIdEntity;
import com.jbtits.github2telegram.persistence.entity.Tribe;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tlgrm_chats")
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class TlgrmChat extends BaseIdEntity {

  @ToString.Include
  @EqualsAndHashCode.Include
  @Column(name = "tlgrm_chat_id")
  private long tlgrmChatId;

  @OneToOne
  @JoinColumn(name = "tribe_id")
  private Tribe tribe;
}
