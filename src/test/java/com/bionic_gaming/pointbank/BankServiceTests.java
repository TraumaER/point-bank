package com.bionic_gaming.pointbank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bionic_gaming.pointbank.api.models.SpendRequest;
import com.bionic_gaming.pointbank.api.models.SpendTransaction;
import com.bionic_gaming.pointbank.api.models.TransactionRequest;
import com.bionic_gaming.pointbank.services.BankService;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;


@Slf4j
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class BankServiceTests {

  @Autowired
  private BankService bankService;

  private void loadTransactions() {
    List<TransactionRequest> preRequests = new ArrayList<>();
    preRequests.add(
        new TransactionRequest("foo", 100, ZonedDateTime.parse("2021-03-03T12:00:00Z")));
    preRequests.add(
        new TransactionRequest("bar", 1000, ZonedDateTime.parse("2021-02-02T12:00:00Z")));
    preRequests.add(
        new TransactionRequest("foo", 200, ZonedDateTime.parse("2021-01-01T12:00:00Z")));
    preRequests.add(
        new TransactionRequest("foo", 400, ZonedDateTime.parse("2021-03-04T12:00:00Z")));

    for (TransactionRequest transactionRequest : preRequests) {
      bankService.addTransaction(transactionRequest);
    }
  }

  @Test
  void pointBalanceReturns() {
    loadTransactions();
    Map<String, Integer> response = bankService.getPayerPointBalances();

    assertThat(response.toString()).isEqualTo("{bar=1000, foo=700}");
  }

  @Test
  void canAddTransaction() {
    bankService.addTransaction(new TransactionRequest("baz", 100, ZonedDateTime.now()));
    Map<String, Integer> response = bankService.getPayerPointBalances();

    assertThat(response.toString()).isEqualTo("{baz=100}");
  }

  @Test
  void spendsOldestFirst() {
    loadTransactions();
    assertThat(bankService.getPayerPointBalances().toString()).isEqualTo("{bar=1000, foo=700}");

    bankService.spendPoints(new SpendRequest(200));

    assertThat(bankService.getPayerPointBalances().toString()).isEqualTo("{bar=1000, foo=500}");

    bankService.spendPoints(new SpendRequest(200));

    assertThat(bankService.getPayerPointBalances().toString()).isEqualTo("{bar=800, foo=500}");

  }

  @Test
  void spreadsPointsAcrossTransactions() {
    loadTransactions();
    assertThat(bankService.getPayerPointBalances().toString()).isEqualTo("{bar=1000, foo=700}");
    List<SpendTransaction> response = bankService.spendPoints(new SpendRequest(400));
    assertThat(bankService.getPayerPointBalances().toString()).isEqualTo("{bar=800, foo=500}");
    assertThat(response.toString()).isEqualTo(
        "[SpendTransaction(payer=foo, points=-200), SpendTransaction(payer=bar, points=-200)]");
  }

  @Test
  void tryingToSpendMoreThanAvailableThrowsException() {
    bankService.addTransaction(new TransactionRequest("baz", 100, ZonedDateTime.now()));

    assertThatThrownBy(() -> bankService.spendPoints(new SpendRequest(500)))
        .isInstanceOf(IllegalArgumentException.class);
  }

}
