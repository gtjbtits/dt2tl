package com.jbtits.github2telegram.persistence.entity.tlgrm;

import com.jbtits.github2telegram.persistence.entity.BaseIdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "tlgrm_users")
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class TlgrmUser extends BaseIdEntity {

  @ToString.Include
  @EqualsAndHashCode.Include
  @Column(name = "tlgrm_user_id")
  private long tlgrmUserId;

  @OneToMany(mappedBy = "tlgrmUser", orphanRemoval = true)
  @Cascade(value = {CascadeType.MERGE, CascadeType.PERSIST})
  private List<TlgrmUserFellow> tlgrmUserFellows;
}
