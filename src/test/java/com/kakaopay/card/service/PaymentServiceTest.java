package com.kakaopay.card.service;

import com.kakaopay.card.domain.PaymentType;
import com.kakaopay.card.domain.cardinfo.CardInfo;
import com.kakaopay.card.domain.cardinfo.CardInfoRepository;
import com.kakaopay.card.domain.payment.Payment;
import com.kakaopay.card.domain.payment.PaymentRepository;
import com.kakaopay.card.service.payment.PaymentService;
import com.kakaopay.card.web.dto.PaymentRequestDto;
import com.kakaopay.card.web.dto.PaymentResponseDto;
import com.kakaopay.card.web.dto.SearchReqeustDto;
import com.kakaopay.card.web.dto.SearchResponseDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

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
                .cardnum(cardnum)
                .expired(expired)
                .cvc(cvc)
                .installment(installment)
                .amount(amount)
                .vat(vat)
                .build());

        String managementId1 = paymentResult.getPaymentId();

        //when
        SearchReqeustDto searchReqeustDto = SearchReqeustDto.builder()
                .managementId(managementId1)
                .build()
                ;

        SearchResponseDto searchResponseDto = paymentService.search(searchReqeustDto);

        //then
        assertThat(searchResponseDto.getPaymentType()).isEqualTo(paymentType.getTypeKorName());
        assertThat(searchResponseDto.getManagementId()).isEqualTo(paymentId);
        assertThat(searchResponseDto.getCardNum().length()).isEqualTo(cardnum.length());
        assertThat(searchResponseDto.getExpired()).isEqualTo(expired);
        assertThat(searchResponseDto.getCvc()).isEqualTo(cvc);
        assertThat(searchResponseDto.getAmount()).isEqualTo(amount);
        assertThat(searchResponseDto.getVat()).isEqualTo(vat);
    }
}
