package com.kakaopay.card.domain.payment;

import com.kakaopay.card.domain.BaseTimeEntity;
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

    // 관리번호
    @Column(length = 20, nullable = false)
    private String paymentId;

    // 카드번호
    @Column(length = 16, nullable = false)
    private String cardnum;

    // 유효기간
    @Column(length = 4, nullable = false)
    private String expired;

    // CVC
    @Column(length = 3, nullable = false)
    private String cvc;

    // 할부개월(0이면 일시불)
    @Column(length = 2, nullable = false)
    private String installment;

    // 금액
    @Column(length = 10, nullable = false)
    private String amount;

    // 부가가치세
    @Column(length = 10, nullable = true)
    private String vat;

    @Builder
    public Payment(String paymentId, String cardnum, String expired, String cvc, String installment, String amount, String vat) {
        this.paymentId = paymentId;
        this.cardnum = cardnum;
        this.expired = expired;
        this.cvc = cvc;
        this.installment = installment;
        this.amount = amount;
        this.vat = vat;
    }
}
