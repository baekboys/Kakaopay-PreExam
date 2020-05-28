package com.kakaopay.card.domain.payment;

import com.kakaopay.card.domain.BaseTimeEntity;
import com.kakaopay.card.domain.PaymentType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Payment extends BaseTimeEntity {

    // 자동채번 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 결제타입 (결제 PAYMENT, 취소 CALCEL)
    @Column(nullable = false)
    private PaymentType paymentType;

    // 결제관리번호
    @Column(length = 20, nullable = false)
    private String paymentId;

    // 취소관리번호
    @Column(length = 20)
    private String cancelId;

    // 카드번호
    @Column(nullable = false)
    private String cardnum;

    // 유효기간
    @Column(nullable = false)
    private String expired;

    // CVC
    @Column(nullable = false)
    private String cvc;

    // 할부개월(0이면 일시불, 취소건은 00이면 일시불)
    @Column(length = 2, nullable = false)
    private String installment;

    // 금액
    @Column(length = 10, nullable = false)
    private long amount;

    // 부가가치세
    @Column(length = 10)
    private long vat;

    @Builder
    public Payment(PaymentType paymentType, String paymentId, String cancelId, String cardnum, String expired, String cvc, String installment, String amount, String vat) {
        this.paymentType = paymentType;
        this.paymentId = paymentId;
        this.cancelId = cancelId;
        this.cardnum = cardnum;
        this.expired = expired;
        this.cvc = cvc;
        this.installment = installment;
        this.amount = Long.valueOf(amount);
        this.vat = Long.valueOf(vat);
    }
}
