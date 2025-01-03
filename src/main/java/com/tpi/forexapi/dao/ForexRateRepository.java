package com.tpi.forexapi.dao;

import com.tpi.forexapi.model.ForexRate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ForexRate DAO
 */
@Repository
public interface ForexRateRepository extends MongoRepository<ForexRate, String> {
    ForexRate findByDate(LocalDateTime date);

    List<ForexRate> findByDateBetween(LocalDateTime dateAfter, LocalDateTime dateBefore);
}
