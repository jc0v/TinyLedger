package com.teya.Ledger.controller;

import com.teya.Ledger.model.Account;
import com.teya.Ledger.model.Transaction;
import com.teya.Ledger.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestParam(required = false) BigDecimal initialBalance) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(initialBalance));
    }

    @GetMapping("/{accountId}/balance")
    public BigDecimal getBalance(@PathVariable Integer accountId) {
        return accountService.getBalance(accountId);
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionsForAccount(@PathVariable Integer accountId) {
        return ResponseEntity.ok(accountService.getTransactions(accountId));
    }

    @PostMapping("/{accountId}/transaction/{amount}")
    public ResponseEntity<Transaction> createTransaction(@PathVariable Integer accountId, @PathVariable BigDecimal amount) {
        return ResponseEntity.ok(accountService.createTransaction(accountId, amount));
    }

    @PostMapping("/{accountId}/transfer/{toAccountId}/{amount}")
    public ResponseEntity<Transaction> transfer(@PathVariable Integer accountId, @PathVariable Integer toAccountId, @PathVariable BigDecimal amount) {
        return ResponseEntity.ok(accountService.transfer(accountId, toAccountId, amount));
    }

}
