package com.example.cognit_invest.repositories.auth;

import com.example.cognit_invest.models.auth.RecoverToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RecoverTokenRepository extends MongoRepository<RecoverToken,String> {
    Optional<RecoverToken> findByCode(String code);
    void deleteAllByUserId(String userId);
}
