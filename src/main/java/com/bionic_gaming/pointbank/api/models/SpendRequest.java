package com.bionic_gaming.pointbank.api.models;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class SpendRequest {

  int points;
}
