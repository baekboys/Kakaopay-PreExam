package com.kakaopay.card.domain.cardinfo;

import org.apache.commons.lang3.StringUtils;

public class CardInfoDataBuider {

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

    public static String getCardInfoDataByTransactionDto(TransactionDto transactionDto) {

        // 문자열데이터 생성
        StringBuilder sb = new StringBuilder(i_totalLength);
        sb
                //  공통헤더부문
                .append(StringUtils.leftPad(Integer.toString(i_totalLength - i_dataLength), i_dataLength, " "))
                .append(StringUtils.rightPad(transactionDto.getPaymentType(), i_dataType, " "))
                .append(StringUtils.rightPad(transactionDto.getManagementId(), i_paymentId, " "))
                // 결제정보
                .append(StringUtils.rightPad(transactionDto.getCardnum(), i_cardnum, " "))
                .append(StringUtils.leftPad(transactionDto.getInstallment(), i_installment, "0"))
                .append(StringUtils.rightPad(transactionDto.getExpired(), i_expired, " "))
                .append(StringUtils.rightPad(transactionDto.getCvc(), i_cvc, " "))
                .append(StringUtils.leftPad(transactionDto.getAmount(), i_amount, " "))
                .append(StringUtils.leftPad(transactionDto.getVat(), i_vat, "0"))
                // 원거래관리번호 (취소건일경우에만 적재)
                .append(StringUtils.leftPad(transactionDto.getOriManagementId(), i_orgPaymentId, " "))
                // 암호화된 카드정보
                .append(StringUtils.rightPad(transactionDto.getEncryptInfo(), i_encryptInfo, " "))
                // 예비 필드
                .append(StringUtils.rightPad(" ", i_filler, " "))
                ;

        String result = sb.toString();

        return result;
    }
}
