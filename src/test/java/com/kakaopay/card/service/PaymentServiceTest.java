package com.kakaopay.card.service;

import com.kakaopay.card.domain.cardinfo.CardInfo;
import com.kakaopay.card.domain.cardinfo.CardInfoRepository;
import com.kakaopay.card.domain.payment.Payment;
import com.kakaopay.card.domain.payment.PaymentRepository;
import com.kakaopay.card.service.payment.PaymentService;
import com.kakaopay.card.web.dto.PaymentRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        //given
        String cardnum = "1234567890123456";
        String expired = "1125";
        String cvc = "777";
        String installment = "0";
        String amount = "110000";
        String vat = "10000";

        PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder()
                .cardnum(cardnum)
                .expired(expired)
                .cvc(cvc)
                .installment(installment)
                .amount(amount)
                .vat(vat)
                .build()
                ;

        //when
        Payment payment = paymentService.save(paymentRequestDto);
        List<Payment> paymentList = paymentRepository.findAll();
        List<CardInfo> cardInfoList = cardInfoRepository.findAll();

        //then
        Payment paymentGetObj = paymentList.get(0);
        CardInfo cardinfoGetObj = cardInfoList.get(0);
        assertThat(paymentGetObj.getPaymentId()).isEqualTo(payment.getPaymentId());
        assertThat(cardinfoGetObj.getPaymentId()).isEqualTo(payment.getPaymentId());
        assertThat(cardinfoGetObj.getPaymentId().length()).isEqualTo(20);
        assertThat(cardinfoGetObj.getData().length()).isEqualTo(450);
    }

    @Test
    public void paymentSave_cardnum10() {
        //given
        String cardnum = "1234567890";
        String expired = "0126";
        String cvc = "321";
        String installment = "03";
        String amount = "13350000";
        String vat = "10000";

        PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder()
                .cardnum(cardnum)
                .expired(expired)
                .cvc(cvc)
                .installment(installment)
                .amount(amount)
                .vat(vat)
                .build()
                ;

        //when
        Payment payment = paymentService.save(paymentRequestDto);
        List<Payment> paymentList = paymentRepository.findAll();
        List<CardInfo> cardInfoList = cardInfoRepository.findAll();

        //then
        Payment paymentGetObj = paymentList.get(0);
        CardInfo cardinfoGetObj = cardInfoList.get(0);
        assertThat(paymentGetObj.getPaymentId()).isEqualTo(payment.getPaymentId());
        assertThat(cardinfoGetObj.getPaymentId()).isEqualTo(payment.getPaymentId());
        assertThat(cardinfoGetObj.getPaymentId().length()).isEqualTo(20);
        assertThat(cardinfoGetObj.getData().length()).isEqualTo(450);
    }
}
