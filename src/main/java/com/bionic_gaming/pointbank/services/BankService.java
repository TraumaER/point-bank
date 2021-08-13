package com.bionic_gaming.pointbank.services;

import com.bionic_gaming.pointbank.api.models.PayerBalance;
import com.bionic_gaming.pointbank.api.models.SpendRequest;
import com.bionic_gaming.pointbank.api.models.TransactionRequest;
import com.bionic_gaming.pointbank.database.entities.Payer;
import com.bionic_gaming.pointbank.database.entities.Transaction;
import com.bionic_gaming.pointbank.database.repositories.PayerRepository;
import com.bionic_gaming.pointbank.database.repositories.TransactionRepository;
import com.google.common.base.Preconditions;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BankService {

  @Autowired
  private final TransactionRepository transactionRepository;

  @Autowired
  private final PayerRepository payerRepository;

  public List<PayerBalance> getPayerPointBalances() {
    List<Payer> payerRows = payerRepository.findAll();

    Map<Payer, Integer> payerMap = new HashMap<>();

    payerRows.forEach(payer -> {
      List<Transaction> transactionRows = transactionRepository.findAllByPayer(payer);
      Integer pointTotal = transactionRows.stream().map(Transaction::getPoints)
          .reduce(0, Integer::sum);
      payerMap.put(payer, pointTotal);
    });

    List<PayerBalance> payerBalances = new ArrayList<>();

    payerMap.forEach((payer, points) -> payerBalances.add(
        PayerBalance.builder().payer(payer.getDisplayName()).points(points).build()));

    return payerBalances;
  }

  public void addTransaction(TransactionRequest request) {
    Preconditions.checkArgument(StringUtils.isNotBlank(request.getPayer()), "'payer' missing");
    Preconditions.checkArgument(request.getPoints() > 0, "'points' must be greater than 0");

    Payer payer = payerRepository.findByName(request.getPayer().toLowerCase());

    if (payer == null) {
      payer = new Payer(request.getPayer());
      payerRepository.save(payer);
    }

    ZonedDateTime zonedDateTime =
        request.getTimestamp() != null ? request.getTimestamp() : ZonedDateTime.now();

    Transaction transaction = new Transaction(payer, request.getPoints(), zonedDateTime);
    transactionRepository.save(transaction);
  }

  public List<PayerBalance> spendPoints(SpendRequest request) {
    return Collections.emptyList();
  }
}
