package com.demo.advanced.repository;

import com.demo.advanced.entities.AccountBankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountBankRepository extends JpaRepository<AccountBankEntity, Long> {

	Optional<AccountBankEntity> findByNumber(Long number);

	@Modifying
	@Transactional
	@Query("UPDATE AccountBankEntity ab SET ab.balance = ?1 WHERE ab.id = ?2")
	void updateBalanceById(BigDecimal balance, Long id);

	List<AccountBankEntity> findAllByClientId(Long clientId);

}
