package com.example.cognit_invest.repositories.auth;

import com.example.cognit_invest.models.auth.LoginRegister;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LoginRegisterRepository extends MongoRepository<LoginRegister,String> {
}
