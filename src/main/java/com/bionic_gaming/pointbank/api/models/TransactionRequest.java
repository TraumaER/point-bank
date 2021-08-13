package com.bionic_gaming.pointbank.api.models;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionRequest {

  String payer;
  int points;
  ZonedDateTime timestamp;
}
