package com.teya.Ledger.repository;

import com.teya.Ledger.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    List<Transaction> findAllByAccountIdOrderByTransactionDateDesc(Integer accountId);
}
