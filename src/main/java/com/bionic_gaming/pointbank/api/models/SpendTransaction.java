package com.bionic_gaming.pointbank.api.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SpendTransaction {
  private final String payer;
  private final int points;
}
