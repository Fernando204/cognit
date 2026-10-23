package com.example.cognit_invest.repositories.finance;

import com.example.cognit_invest.models.finance.BankAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BankAccountRepository extends MongoRepository<BankAccount,String> {
    List<BankAccount> findAllByUserIdAndDeletedAtIsNull(String userId);
}
