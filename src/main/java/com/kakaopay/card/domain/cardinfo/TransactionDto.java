package com.kakaopay.card.domain.cardinfo;

import com.kakaopay.card.domain.BaseTimeEntity;
import com.kakaopay.card.domain.PaymentType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
public class TransactionDto {

    // 결제타입 (결제 PAYMENT, 취소 CALCEL)
    private String paymentType;

    // 관리번호
    private String managementId;

    // 원관리번호
    private String oriManagementId;

    // 카드번호
    private String cardnum;

    // 유효기간
    private String expired;

    // CVC
    private String cvc;

    // 할부개월(0이면 일시불)
    private String installment;

    // 금액
    private String amount;

    // 부가가치세
    private String vat;

    // 암호화된 카드정보
    private String encryptInfo;

    @Builder
    public TransactionDto(String paymentType, String managementId, String oriManagementId, String cardnum, String expired, String cvc, String installment, String amount, String vat, String encryptInfo) {
        this.paymentType = paymentType;
        this.managementId = managementId;
        this.oriManagementId = oriManagementId;
        this.cardnum = cardnum;
        this.expired = expired;
        this.cvc = cvc;
        this.installment = installment;
        this.amount = amount;
        this.vat = vat;
        this.encryptInfo = encryptInfo;
    }
}
