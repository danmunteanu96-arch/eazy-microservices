package com.eazy.accounts.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eazy.accounts.entity.Accounts;

@Repository
public interface AccountRepository extends CrudRepository<Accounts, Long> {

    @Transactional
    @Query("SELECT a from Accounts a WHERE a.customerId = :customerId")
    Optional<Accounts> findByCustomerId(Long customerId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Accounts a WHERE a.customerId = :customerId")
    void deleteByCustomerId(Long customerId);
}
