package com.bionic_gaming.pointbank.controllers;

import com.bionic_gaming.pointbank.api.models.SpendTransaction;
import com.bionic_gaming.pointbank.api.models.SpendRequest;
import com.bionic_gaming.pointbank.api.models.TransactionRequest;
import com.bionic_gaming.pointbank.services.BankService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BankController {

  private final BankService bankService;

  @RequestMapping("/")
  String home() {
    return "Point Bank Service is running...";
  }

  @GetMapping("/balances")
  Map<String, Integer> allPayerBalances() {
    return bankService.getPayerPointBalances();
  }

  @PostMapping("/add-transaction")
  void addTransaction(@RequestBody TransactionRequest request) {
    bankService.addTransaction(request);
  }

  @PostMapping("/spend")
  List<SpendTransaction> spendPoints(@RequestBody SpendRequest request) {
    return bankService.spendPoints(request);
  }
}
