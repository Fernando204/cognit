package com.example.cognit_invest.repositories.auth;

import com.example.cognit_invest.models.auth.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token,String> {
    Optional<Token> findByToken(String token);
}
