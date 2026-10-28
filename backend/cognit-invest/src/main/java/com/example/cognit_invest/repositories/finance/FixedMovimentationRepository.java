package com.example.cognit_invest.repositories.finance;

import com.example.cognit_invest.models.finance.FixedMovimentation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface FixedMovimentationRepository extends MongoRepository<FixedMovimentation, String> {

    List<FixedMovimentation> findByUserIdAndDeletedAtIsNull(String userId);

    Optional<FixedMovimentation> findByIdAndUserIdAndDeletedAtIsNull(String id, String userId);

    List<FixedMovimentation> findByDeletedAtIsNullAndNextCreationLessThan(Instant limit);
}