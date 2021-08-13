package com.bionic_gaming.pointbank.database.entities;

import com.google.common.base.Preconditions;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(insertable = false, updatable = false)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "payerId")
  private Payer payer;
  private Integer points;
  private ZonedDateTime timestamp;

  public Transaction(Payer payer, Integer points, ZonedDateTime timestamp) {
    Preconditions.checkArgument(payer != null, "'payer' cannot be null");
    this.payer = payer;
    this.points = points;
    this.timestamp = timestamp;
  }
}
