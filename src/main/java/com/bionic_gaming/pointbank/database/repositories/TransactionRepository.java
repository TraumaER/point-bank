package com.bionic_gaming.pointbank.database.repositories;

import com.bionic_gaming.pointbank.database.entities.Payer;
import com.bionic_gaming.pointbank.database.entities.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  List<Transaction> findAllByPayer(Payer payer);
}
