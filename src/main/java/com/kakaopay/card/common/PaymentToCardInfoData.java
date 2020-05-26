package com.kakaopay.card.common;

import com.kakaopay.card.common.encrypt.KISASeedEncryptor;
import com.kakaopay.card.domain.PaymentType;
import com.kakaopay.card.domain.payment.Payment;
import org.apache.commons.lang3.StringUtils;

public class PaymentToCardInfoData {

    final static int i_dataLength = 4;
    final static int i_dataType = 10;
    final static int i_paymentId = 20;
    final static int i_cardnum = 20;
    final static int i_installment = 2;
    final static int i_expired = 4;
    final static int i_cvc = 3;
    final static int i_amount = 10;
    final static int i_vat = 10;
    final static int i_orgPaymentId = 20;
    final static int i_encryptInfo = 300;
    final static int i_filler = 47;
    final static int i_totalLength = 450;

    public static String doChangePaymentToCardInfoData(Payment payment) {
        String result = "";
        StringBuilder sb = new StringBuilder(i_totalLength);

        sb
                //  공통헤더부문
                .append(StringUtils.leftPad(Integer.toString(i_totalLength - i_dataLength), i_dataLength, " "))
                .append(StringUtils.rightPad(PaymentType.PAYMENT.getTypeName(), i_dataType, " "))
                .append(StringUtils.rightPad(payment.getPaymentId(), i_paymentId, " "))
                // 결제정보
                .append(StringUtils.rightPad(payment.getCardnum(), i_cardnum, " "))
                .append(StringUtils.leftPad(payment.getInstallment(), i_installment, "0"))
                .append(StringUtils.rightPad(payment.getExpired(), i_expired, " "))
                .append(StringUtils.rightPad(payment.getCvc(), i_cvc, " "))
                .append(StringUtils.leftPad(Long.toString(payment.getAmount()), i_amount, " "))
                .append(StringUtils.leftPad(Long.toString(payment.getVat()), i_vat, "0"))
                // 원거래관리번호 (취소건일경우에만 적재)
                .append(StringUtils.leftPad(" ", i_orgPaymentId, " "))
                // 암호화된 카드정보
                .append(StringUtils.rightPad(payment.getEncryptInfo(), i_encryptInfo, " "))
                // 예비 필드
                .append(StringUtils.rightPad(" ", i_filler, " "))
                ;

        result = sb.toString();

        return result;
    }
}
