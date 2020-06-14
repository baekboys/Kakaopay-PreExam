package com.kakaopay.card.service;

import com.kakaopay.card.Exception.BizExceptionType;
import com.kakaopay.card.Exception.SearchBizException;
import com.kakaopay.card.common.encrypt.KISASeedEncryptor;
import com.kakaopay.card.domain.PaymentType;
import com.kakaopay.card.domain.cardinfo.CardInfoRepository;
import com.kakaopay.card.domain.payment.Payment;
import com.kakaopay.card.domain.payment.PaymentRepository;
import com.kakaopay.card.service.payment.PaymentService;
import com.kakaopay.card.web.dto.*;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentServiceTest {

    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    CardInfoRepository cardInfoRepository;

    @After
    public void cleanup() {
        paymentRepository.deleteAll();
        cardInfoRepository.deleteAll();
    }

    @Test
    public void paymentSave_cardnum16() {

    }

    @Test
    public void paymentSave_cardnum10() {

    }

    @Test
    public void validPaymentIdSearchTest() {

        //given
        PaymentType paymentType = PaymentType.PAYMENT;

        String paymentId = "XXXXXXXXXXXXXXXXXXXX";
        String cancelId = "";

        String plainCardNum = "1234567890123456";
        String plainExpired = "1125";
        String plainCvc = "777";

        String cardnum = KISASeedEncryptor.encrypt(plainCardNum);
        String expired = KISASeedEncryptor.encrypt(plainExpired);
        String cvc = KISASeedEncryptor.encrypt(plainCvc);
        String installment = "0";
        String amount = "100000";
        String vat = "10000";

        Payment paymentResult = paymentRepository.save(Payment.builder()
                .paymentType(paymentType)
                .paymentId(paymentId)
                .cancelId(cancelId)
                .cardnum(cardnum)
                .expired(expired)
                .cvc(cvc)
                .installment(installment)
                .amount(amount)
                .vat(vat)
                .build());

        String managementId1 = paymentResult.getPaymentId();

        //when
        SearchResponseDto searchResponseDto = paymentService.search(managementId1);

        //then
        assertThat(searchResponseDto.getPaymentType()).isEqualTo(paymentType.getTypeKorName());
        assertThat(searchResponseDto.getManagementId()).isEqualTo(managementId1);
        assertThat(searchResponseDto.getCardNum().length()).isEqualTo(plainCardNum.length());
        assertThat(searchResponseDto.getExpired()).isEqualTo(plainExpired);
        assertThat(searchResponseDto.getCvc()).isEqualTo(plainCvc);
        assertThat(searchResponseDto.getAmount()).isEqualTo(amount);
        assertThat(searchResponseDto.getVat()).isEqualTo(vat);
    }

    @Test
    public void invalidPaymentIdSearchTest() {

        //given
        PaymentType paymentType = PaymentType.PAYMENT;

        String paymentId = "XXXXXXXXXXXXXXXXXXXX";
        String cancelId = "";

        String plainCardNum = "1234567890123456";
        String plainExpired = "1125";
        String plainCvc = "777";

        String cardnum = KISASeedEncryptor.encrypt(plainCardNum);
        String expired = KISASeedEncryptor.encrypt(plainExpired);
        String cvc = KISASeedEncryptor.encrypt(plainCvc);
        String installment = "0";
        String amount = "100000";
        String vat = "10000";

        Payment paymentResult = paymentRepository.save(Payment.builder()
                .paymentType(paymentType)
                .paymentId(paymentId)
                .cancelId(cancelId)
                .cardnum(cardnum)
                .expired(expired)
                .cvc(cvc)
                .installment(installment)
                .amount(amount)
                .vat(vat)
                .build());

        String managementId1 = "ZZZZZZZZZZZZZZZZZZZZ";

        //when
        BizExceptionType bizExceptionType = null;
        SearchResponseDto searchResponseDto = null;
        try {
            searchResponseDto = paymentService.search(managementId1);
        } catch (SearchBizException se) {
            bizExceptionType = se.getBizExceptionType();
        }

        //then
        assertThat(searchResponseDto).isNull();
        assertThat(bizExceptionType).isNotNull();
        assertThat(bizExceptionType).isEqualTo(BizExceptionType.DATA_NOT_FONUD);
    }

    @Test
    public void validCancelIdSearchTest() {

        //given
        PaymentType paymentType = PaymentType.CANCEL;

        String paymentId = "XXXXXXXXXXXXXXXXXXXX";
        String cancelId = "ZZZZZZZZZZZZZZZZZZZZ";

        String cardnum = "1234567890123456";
        String expired = "1125";
        String cvc = "777";
        String installment = "0";
        String amount = "100000";
        String vat = "10000";

        Payment paymentResult = paymentRepository.save(Payment.builder()
                .paymentType(paymentType)
                .paymentId(paymentId)
                .cancelId(cancelId)
                .cardnum(KISASeedEncryptor.encrypt(cardnum))
                .expired(KISASeedEncryptor.encrypt(expired))
                .cvc(KISASeedEncryptor.encrypt(cvc))
                .installment(installment)
                .amount(amount)
                .vat(vat)
                .build());

        String managementId1 = "ZZZZZZZZZZZZZZZZZZZZ";

        //when
        SearchResponseDto searchResponseDto = paymentService.search(managementId1);


        //then
        assertThat(searchResponseDto.getPaymentType()).isEqualTo(paymentType.getTypeKorName());
        assertThat(searchResponseDto.getManagementId()).isEqualTo(managementId1);
        assertThat(searchResponseDto.getCardNum().length()).isEqualTo(cardnum.length());
        assertThat(searchResponseDto.getExpired()).isEqualTo(expired);
        assertThat(searchResponseDto.getCvc()).isEqualTo(cvc);
        assertThat(searchResponseDto.getAmount()).isEqualTo(amount);
        assertThat(searchResponseDto.getVat()).isEqualTo(vat);
    }

    @Test
    public void PayAndCancelIdSearchTest() {

        String paymentId = "XXXXXXXXXXXXXXXXXXXX";
        String cancelId = "ZZZZZZZZZZZZZZZZZZZZ";

        String cardnum = "1234567890123456";
        String expired = "1125";
        String cvc = "777";
        String installment = "0";
        String amount = "100000";
        String vat = "10000";

        //given
        {
            PaymentType paymentType = PaymentType.PAYMENT;
            Payment paymentResult = paymentRepository.save(Payment.builder()
                    .paymentType(paymentType)
                    .paymentId(paymentId)
                    .cancelId("")
                    .cardnum(KISASeedEncryptor.encrypt(cardnum))
                    .expired(KISASeedEncryptor.encrypt(expired))
                    .cvc(KISASeedEncryptor.encrypt(cvc))
                    .installment(installment)
                    .amount(amount)
                    .vat(vat)
                    .build());
        }

        {
            PaymentType paymentType = PaymentType.CANCEL;
            Payment paymentResult = paymentRepository.save(Payment.builder()
                    .paymentType(paymentType)
                    .paymentId(paymentId)
                    .cancelId(cancelId)
                    .cardnum(KISASeedEncryptor.encrypt(cardnum))
                    .expired(KISASeedEncryptor.encrypt(expired))
                    .cvc(KISASeedEncryptor.encrypt(cvc))
                    .installment(installment)
                    .amount(amount)
                    .vat(vat)
                    .build());
        }

        String managementId1 = "XXXXXXXXXXXXXXXXXXXX";

        //when
        SearchResponseDto searchResponseDto = paymentService.search(managementId1);

        //then
        assertThat(searchResponseDto.getPaymentType()).isEqualTo(PaymentType.PAYMENT.getTypeKorName());
        assertThat(searchResponseDto.getManagementId()).isEqualTo(managementId1);
        assertThat(searchResponseDto.getCardNum().length()).isEqualTo(cardnum.length());
        assertThat(searchResponseDto.getExpired()).isEqualTo(expired);
        assertThat(searchResponseDto.getCvc()).isEqualTo(cvc);
        assertThat(searchResponseDto.getAmount()).isEqualTo(amount);
        assertThat(searchResponseDto.getVat()).isEqualTo(vat);
    }

    @Test
    public void paymentAndCancelTest() {

        String paymentId = "";
        PaymentResponseDto paymentResponseDto = null;

        String cancelId = "";
        CancelResponseDto cancelResponseDto = null;

        //given
        {
            String cardnum = "1234567890123456";
            String expired = "1125";
            String cvc = "777";
            String installment = "0";
            String amount = "100000";
            String vat = "10000";

            paymentResponseDto = paymentService.save(PaymentRequestDto.builder()
                    .cardnum(cardnum)
                    .expired(expired)
                    .cvc(cvc)
                    .installment(installment)
                    .amount(amount)
                    .vat(vat)
                    .build()
            );
            paymentId = paymentResponseDto.getManagementId();
        }


        //when
        {
            String amount = "100000";
            String vat = "10000";
            cancelResponseDto = paymentService.cancel(CancelReqeustDto.builder()
                    .managementId(paymentId)
                    .amount(amount)
                    .vat(vat)
                    .build()
            );
            cancelId = cancelResponseDto.getManagementId();
        }

        //when
        SearchResponseDto searchResponseDto = paymentService.search(cancelId);

        //then
        assertThat(paymentId).isNotEmpty();
        assertThat(cancelId).isNotEmpty();

        assertThat(paymentId.length()).isEqualTo(20);
        assertThat(cancelId.length()).isEqualTo(20);

        assertThat(paymentId).isNotEqualTo(cancelId);

        assertThat(paymentResponseDto).isNotNull();
        assertThat(cancelResponseDto).isNotNull();

        assertThat(searchResponseDto.getManagementId().length()).isEqualTo(20);
        assertThat(searchResponseDto.getManagementId()).isNotEqualTo(paymentId);
        assertThat(searchResponseDto.getTransactionTime()).isNotNull();
        assertThat(searchResponseDto.getPaymentType()).isEqualTo(PaymentType.CANCEL.getTypeKorName());
    }
}
