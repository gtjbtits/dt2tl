package com.jbtits.github2telegram.persistence.entity.tlgrm;

import com.jbtits.github2telegram.persistence.entity.BaseIdEntity;
import com.jbtits.github2telegram.persistence.entity.Fellow;
import com.jbtits.github2telegram.persistence.entity.Tribe;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tlgrm_user_fellows")
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class TlgrmUserFellow extends BaseIdEntity {

  @ManyToOne
  @JoinColumn(name = "tu_id")
  @ToString.Include
  private TlgrmUser tlgrmUser;

  @OneToOne
  @JoinColumn(name = "fellow_id")
  @ToString.Include
  private Fellow fellow;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "tribe_id")
  @ToString.Include
  private Tribe tribe;
}
