package com.kakaopay.card.web;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Before
    public void setup() {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void paymentSaveTest() throws Exception {
        //given
        String cardnum = "";
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


        String url = "http://localhost:" + port + "/api/v1/payment";

        // when
        ResponseEntity<PaymentResponseDto> responseEntity = restTemplate.postForEntity(url, paymentRequestDto, PaymentResponseDto.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getPaymentId().length()).isEqualTo(20);
    }
}
