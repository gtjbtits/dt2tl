package com.jbtits.github2telegram.persistence.entity.tlgrm;

import com.jbtits.github2telegram.persistence.entity.BaseIdEntity;
import com.jbtits.github2telegram.persistence.entity.Fellow;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Data
@Entity
@Table(name = "tlgrm_users")
@EqualsAndHashCode(callSuper = true)
public class TlgrmUser extends BaseIdEntity {
  @Column(name = "tlgrm_id")
  private long telegramId;

  @OneToMany
  @JoinTable(name = "tlgrm_fellows",
      joinColumns = @JoinColumn(name = "tlgrm_user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "fellow_id", referencedColumnName = "id"))
  private Set<Fellow> fellows;
}
