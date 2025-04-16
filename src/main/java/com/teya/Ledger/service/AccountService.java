package com.teya.Ledger.service;

import com.teya.Ledger.model.Account;
import com.teya.Ledger.model.Transaction;
import com.teya.Ledger.model.TransactionType;
import com.teya.Ledger.repository.AccountRepository;
import com.teya.Ledger.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public Account createAccount(final BigDecimal initialBalance) {
        Account account = new Account();
        account.setBalance(initialBalance != null && initialBalance.compareTo(BigDecimal.ZERO) > 0
                ? initialBalance : BigDecimal.ZERO);
        accountRepository.save(account);
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            createAndSaveTransaction(account.getBalance(), account);
        }
        return account;
    }

    public BigDecimal getBalance(final Integer accountId) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isPresent()) {
            return accountOptional.get().getBalance();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public List<Transaction> getTransactions(final Integer accountId) {
        List<Transaction> transactions = transactionRepository.findAllByAccountIdOrderByTransactionDateDesc(accountId);
        if (transactions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return transactions;
    }

    public Transaction createTransaction(Integer accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        return createAndSaveTransaction(amount, account);
    }

    public Transaction transfer(Integer accountId, Integer toAccountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        account.setBalance(account.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(account);

        createAndSaveTransaction(amount, toAccount);
        return createAndSaveTransaction(amount.negate(), account);
    }

    private Transaction createAndSaveTransaction(BigDecimal amount, Account account) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount.abs());
        transaction.setAccount(account);
        transaction.setType(amount.compareTo(BigDecimal.ZERO) > 0
                ? TransactionType.CREDIT : TransactionType.DEBIT);
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);
        return transaction;
    }
}
