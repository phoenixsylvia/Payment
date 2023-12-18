package com.example.payment.repository;

import com.example.payment.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByIdAndUserId(long id, Long userId);

    @Query(value = "select * from transactions where status = ?1 ", nativeQuery = true)
    List<Transaction> findByStatus(String name);

    Page<Transaction> findByUserId(Long id, Pageable pageable);
}
