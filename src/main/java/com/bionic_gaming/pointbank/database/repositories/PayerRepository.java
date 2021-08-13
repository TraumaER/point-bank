package com.bionic_gaming.pointbank.database.repositories;

import com.bionic_gaming.pointbank.database.entities.Payer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayerRepository extends JpaRepository<Payer, Long> {

  List<Payer> findAllByOrderByNameAsc();

  Payer findByName(String name);
}
