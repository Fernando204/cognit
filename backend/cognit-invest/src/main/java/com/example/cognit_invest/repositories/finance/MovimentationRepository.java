package com.example.cognit_invest.repositories.finance;

import com.example.cognit_invest.models.finance.Movimentation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MovimentationRepository extends MongoRepository<Movimentation,String> {
    List<Movimentation> findAllByUserIdAndDeletedAtIsNull(String userId);
}
