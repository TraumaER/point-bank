package com.bionic_gaming.pointbank.api.models;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class TransactionRequest {

  String payer;
  int points;
  ZonedDateTime timestamp;
}
