package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.TransactionRecord; // Das hier muss oben stehen!
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<TransactionRecord, Long> {
}