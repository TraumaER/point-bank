package com.bionic_gaming.pointbank.database;

import com.bionic_gaming.pointbank.database.entities.Transaction;
import com.bionic_gaming.pointbank.database.entities.Payer;
import com.bionic_gaming.pointbank.database.repositories.TransactionRepository;
import com.bionic_gaming.pointbank.database.repositories.PayerRepository;
import java.time.ZonedDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {

  @Bean
  CommandLineRunner initDatabase(TransactionRepository transactionRepository, PayerRepository payerRepository) {
    return args -> {
      Payer target = new Payer("Target");
      Payer walmart = new Payer("WalMart");

      log.info("Preloading " + payerRepository.save(target));
      log.info("Preloading " + payerRepository.save(walmart));
      log.info("Preloading " + transactionRepository.save(new Transaction(target, 200, ZonedDateTime.now())));
      log.info("Preloading " + transactionRepository.save(new Transaction(target, 300, ZonedDateTime.now())));
      log.info("Preloading " + transactionRepository.save(new Transaction(walmart, 300, ZonedDateTime.now())));
    };
  }

}
