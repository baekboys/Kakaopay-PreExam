package com.kakaopay.card.domain.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p ORDER BY p.createdDate DESC")
    List<Payment> findAllDesc();

    // 결제관리ID로 데이터 조회
    Optional<Payment> findByPaymentId(String paymentId);

    // 취소관리ID로 데이터 조회
    Optional<Payment> findByCancelId(String cancelId);
}