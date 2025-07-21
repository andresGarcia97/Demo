package com.demo.advanced.repository;

import com.demo.advanced.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {

    List<TransactionEntity> findByOrigin_IdOrDestiny_Id(Long accountOriginId, Long accountDestinyId);

}
