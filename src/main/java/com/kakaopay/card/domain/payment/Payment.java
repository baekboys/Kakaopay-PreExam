package com.kakaopay.card.domain.payment;

import com.kakaopay.card.common.encrypt.KISASeedEncryptor;
import com.kakaopay.card.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.math.BigDecimal;

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
    @Column(nullable = false)
    private String cardnum;

    // 유효기간
    @Column(nullable = false)
    private String expired;

    // CVC
    @Column(nullable = false)
    private String cvc;

    // 할부개월(0이면 일시불)
    @Column(length = 2, nullable = false)
    private String installment;

    // 금액
    @Column(length = 10, nullable = false)
    private long amount;

    // 부가가치세
    @Column(length = 10)
    private long vat;

    @Builder
    public Payment(String paymentId, String cardnum, String expired, String cvc, String installment, String amount, String vat) {
        this.paymentId = paymentId;
        this.cardnum = cardnum;
        this.expired = expired;
        this.cvc = cvc;
        this.installment = installment;
        this.amount = Long.valueOf(amount);
        this.vat = Long.valueOf(vat);
    }

    // 암호화된 카드번호, 유효기간, CVC 리턴 =>  "카드번호,유효기간,CVC" 문자열을 암호화하여 리턴
    public String getEncryptInfo() {
        String encResult = "";

        // 카드번호, 유효기간, CVC 중 하나라도 비어있으면 비어있는 문자열로 리턴
        if(StringUtils.isEmpty(this.cardnum) || StringUtils.isEmpty(this.expired) || StringUtils.isEmpty(this.cvc)) {
            return encResult;
        }

        String plainInfo = this.cardnum + "," + this.expired + "," + this.cvc;
        encResult = KISASeedEncryptor.encrypt(plainInfo);

        return encResult;
    }
}
