package com.kakaopay.card.domain.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p ORDER BY p.id DESC")
    List<Payment> findAllDesc();
}