package com.kakaopay.card.web;

import com.kakaopay.card.domain.payment.PaymentRepository;
import com.kakaopay.card.service.payment.PaymentService;
import com.kakaopay.card.web.dto.PaymentRequestDto;
import com.kakaopay.card.web.dto.PaymentResponseDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    // 정확한 데이터 타입 세팅 카드번호 16자리
    private PaymentRequestDto validPaymentRequestDto16;
    // 정확한 데이터 타입 세팅 카드번호 10자리
    private PaymentRequestDto validPaymentRequestDto10;
    // 부정확한 데이터 타입 세팅 카드번호 9자리
    private PaymentRequestDto invalidPaymentRequestDto9;

    @Before
    public void setup() {

        // 정확한 데이터 타입 세팅 카드번호 16자리
        {
            String cardnum = "1234567890123456";
            String expired = "1125";
            String cvc = "777";
            String installment = "0";
            String amount = "110000";
            String vat = "10000";

            this.validPaymentRequestDto16 = PaymentRequestDto.builder()
                    .cardnum(cardnum)
                    .expired(expired)
                    .cvc(cvc)
                    .installment(installment)
                    .amount(amount)
                    .vat(vat)
                    .build();
        }

        // 정확한 데이터 타입 세팅 카드번호 10자리
        {
            String cardnum = "1234567890";
            String expired = "1125";
            String cvc = "777";
            String installment = "0";
            String amount = "110000";
            String vat = "10000";

            this.validPaymentRequestDto10 = PaymentRequestDto.builder()
                    .cardnum(cardnum)
                    .expired(expired)
                    .cvc(cvc)
                    .installment(installment)
                    .amount(amount)
                    .vat(vat)
                    .build();
        }

        // 부정확한 데이터 타입 세팅 카드번호 9자리
        {
            String cardnum = "123456789";
            String expired = "1125";
            String cvc = "777";
            String installment = "0";
            String amount = "110000";
            String vat = "10000";

            this.validPaymentRequestDto10 = PaymentRequestDto.builder()
                    .cardnum(cardnum)
                    .expired(expired)
                    .cvc(cvc)
                    .installment(installment)
                    .amount(amount)
                    .vat(vat)
                    .build();
        }
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void paymentSaveTest() throws Exception {
        //given
        PaymentRequestDto paymentRequestDto = this.validPaymentRequestDto16;

        String url = "http://localhost:" + port + "/api/v1/payment";

        // when
        ResponseEntity<PaymentResponseDto> responseEntity = restTemplate.postForEntity(url, paymentRequestDto, PaymentResponseDto.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getManagementId().length()).isEqualTo(20);
        assertThat(responseEntity.getBody().getTransactionTime()).isNotNull();
        assertThat(responseEntity.getBody().getErrorCode()).isEmpty();
        assertThat(responseEntity.getBody().getErrorMessage()).isEmpty();
    }
}
