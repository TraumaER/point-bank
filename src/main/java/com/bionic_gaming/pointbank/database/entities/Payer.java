package com.bionic_gaming.pointbank.database.entities;

import com.google.common.base.Preconditions;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@Entity
@NoArgsConstructor
public class Payer {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(insertable = false, updatable = false)
  private Long id;

  @Column(unique = true)
  private String name;

  @Column(unique = true)
  private String displayName;

  public Payer(String name) {
    Preconditions.checkArgument(StringUtils.isNotBlank(name), "'name' cannot be blank");
    this.name = name.toLowerCase();
    this.displayName = name;
  }
}

