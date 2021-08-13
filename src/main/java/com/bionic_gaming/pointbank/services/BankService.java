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
import java.util.Collection;
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

  public Map<String, Integer> getPayerPointBalances() {
    List<Payer> payerRows = payerRepository.findAll();

    Map<String, Integer> payerMap = new HashMap<>();

    payerRows.forEach(payer -> {

      List<Transaction> transactions = transactionRepository.findAllByPayer(payer);

      int total = sumOfPoints(transactions);

      payerMap.put(payer.getDisplayName(), total);
    });

    return payerMap;
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
    Preconditions.checkArgument(request.getPoints() > 0, "'points' must be greater than 0");

    int remainingToSpend = request.getPoints();

    List<Transaction> transactions = transactionRepository.findAllByPointsGreaterThanOrderByTimestampAsc(
        0);
    List<Transaction> negativeTransactions = transactionRepository.findAllByPointsLessThanOrderByTimestampAsc(
        0);

    int totalPointsRemaining = sumOfPoints(transactions) + sumOfPoints(negativeTransactions);

    Preconditions.checkArgument(remainingToSpend <= totalPointsRemaining,
        "attempting to spend more points than are remaining");

    List<PayerBalance> spentPoints = new ArrayList<>();

    for (Transaction transaction : transactions) {

      if (transaction.isFullySpent()) {
        continue;
      }

      if (remainingToSpend == 0) {
        break;
      }

      int transactionBalance =
          transaction.getPoints() + sumOfPoints(transaction.getNegatingTransactions());

      if (remainingToSpend >= transactionBalance) {
        // create negation transaction
        Transaction negate = new Transaction(transaction.getPayer(),
            transactionBalance * -1, ZonedDateTime.now());

        transactionRepository.save(negate);
        // add negation transaction to current transaction
        transaction.getNegatingTransactions().add(negate);
        transaction.setFullySpent(true);

        transactionRepository.save(transaction);

        // add to response
        spentPoints.add(PayerBalance.builder().payer(transaction.getPayer().getDisplayName())
            .points(transactionBalance * -1).build());
        // reduce remaining
        remainingToSpend -= transactionBalance;

      } else {
        Transaction negate = new Transaction(transaction.getPayer(),
            remainingToSpend * -1, ZonedDateTime.now());
        transactionRepository.save(negate);

        transaction.getNegatingTransactions().add(negate);
        transactionRepository.save(transaction);

        spentPoints.add(PayerBalance.builder().payer(transaction.getPayer().getDisplayName())
            .points(remainingToSpend * -1).build());
        remainingToSpend = 0;
      }
    }

    return spentPoints;
  }

  /**
   * Helper function to sum up the points in a collection of transactions
   * @param transactions
   * @return int
   */
  private int sumOfPoints(Collection<Transaction> transactions) {
    return transactions.stream().map(Transaction::getPoints)
        .reduce(0, Integer::sum);
  }
}
