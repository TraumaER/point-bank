package com.bionic_gaming.pointbank.database;

import com.bionic_gaming.pointbank.database.entities.Payer;
import com.bionic_gaming.pointbank.database.entities.Transaction;
import com.bionic_gaming.pointbank.database.repositories.PayerRepository;
import com.bionic_gaming.pointbank.database.repositories.TransactionRepository;
import java.time.ZonedDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {

  @Bean
  CommandLineRunner initDatabase(TransactionRepository transactionRepository,
      PayerRepository payerRepository) {
    return args -> {
      Payer target = new Payer("Target");
      Payer walmart = new Payer("WalMart");

      payerRepository.save(target);
      payerRepository.save(walmart);
      transactionRepository.save(
          new Transaction(target, 500, ZonedDateTime.parse("2021-01-25T15:00:00Z")));
      transactionRepository.save(
          new Transaction(target, 500, ZonedDateTime.parse("2020-12-25T15:00:00Z")));
      transactionRepository.save(
          new Transaction(walmart, 500, ZonedDateTime.parse("2021-01-05T15:00:00Z")));

      Transaction someTransaction = new Transaction(walmart, 1000, ZonedDateTime.now());

      Transaction someNegativeTransaction = new Transaction(walmart, -500, ZonedDateTime.now());
      transactionRepository.save(someNegativeTransaction);

      someTransaction.getNegatingTransactions().add(someNegativeTransaction);
      transactionRepository.save(someTransaction);
    };
  }

}
