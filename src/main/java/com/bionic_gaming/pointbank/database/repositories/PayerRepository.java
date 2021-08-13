package com.bionic_gaming.pointbank.database.repositories;

import com.bionic_gaming.pointbank.database.entities.Payer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayerRepository extends JpaRepository<Payer, Long> {
  Payer findByName(String name);
}
