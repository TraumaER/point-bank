package com.bionic_gaming.pointbank.api.models;

import java.time.ZonedDateTime;
import lombok.Data;

@Data
public class TransactionRequest {

  String payer;
  int points;
  ZonedDateTime timestamp;
}
