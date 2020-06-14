package com.kakaopay.card.web;

import com.kakaopay.card.Exception.BizExceptionType;
import com.kakaopay.card.domain.PaymentType;
import com.kakaopay.card.domain.payment.PaymentRepository;
import com.kakaopay.card.service.payment.PaymentService;
import com.kakaopay.card.web.dto.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
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

            this.invalidPaymentRequestDto9 = PaymentRequestDto.builder()
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
    public void invalidMethodTest() throws Exception {

        //given
        String paymentUrl = "http://localhost:" + port + "/api/v1/payment";
        String cancelUrl = "http://localhost:" + port + "/api/v1/cancel";
        String searchUrl = "http://localhost:" + port + "/api/v1/search";


        //given
        ResponseEntity<PaymentResponseDto> paymentResponseDtoResponseEntity = restTemplate.getForEntity(paymentUrl, PaymentResponseDto.class);

        // then
        assertThat(paymentResponseDtoResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(paymentResponseDtoResponseEntity.getBody().getErrorCode()).isEqualTo(BizExceptionType.HTTP_METHOD_ERROR.getCode());
    }

    @Test
    public void invlidCardNumTest() throws Exception {
        //given
        PaymentRequestDto paymentRequestDto = this.invalidPaymentRequestDto9;

        String url = "http://localhost:" + port + "/api/v1/payment";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PaymentRequestDto> httpEntity = new HttpEntity<>(paymentRequestDto, httpHeaders);

        // when
        ResponseEntity<PaymentResponseDto> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, PaymentResponseDto.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getManagementId().length()).isEqualTo(20);
        assertThat(responseEntity.getBody().getTransactionTime()).isNotNull();
    }

    @Test
    public void paymentSaveTest() throws Exception {
        //given
        PaymentRequestDto paymentRequestDto = this.validPaymentRequestDto16;

        String url = "http://localhost:" + port + "/api/v1/payment";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PaymentRequestDto> httpEntity = new HttpEntity<>(paymentRequestDto, httpHeaders);

        // when
        ResponseEntity<PaymentResponseDto> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, PaymentResponseDto.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getManagementId().length()).isEqualTo(20);
        assertThat(responseEntity.getBody().getTransactionTime()).isNotNull();
    }

    @Test
    public void invalidMngIdSearchTest() throws Exception {
        // 부정확한 관리번호를 넣은 경우
        {
            //given
            String managementId = "zzzzzzzzzz";

            // when
            String searchUrl = "http://localhost:" + port + "/api/v1/search/" + managementId;
            ResponseEntity<SearchResponseDto> searchResponseDtoResponseEntity = restTemplate.getForEntity(searchUrl, SearchResponseDto.class);

            // then
            assertThat(searchResponseDtoResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

            assertThat(searchResponseDtoResponseEntity.getBody().getErrorCode()).isNotEmpty();
            assertThat(searchResponseDtoResponseEntity.getBody().getErrorCode()).isEqualTo(BizExceptionType.INVALID_MANAGEMENT_ID.getCode());
        }

        // 관리번호가 없는 데이터를 조회하는 경우
        {
            //given
            String managementId = "12345678901234567890";

            // when
            String searchUrl = "http://localhost:" + port + "/api/v1/search/"+managementId;
            ResponseEntity<SearchResponseDto> searchResponseDtoResponseEntity = restTemplate.getForEntity(searchUrl, SearchResponseDto.class);

            // then
            assertThat(searchResponseDtoResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

            assertThat(searchResponseDtoResponseEntity.getBody().getErrorCode()).isNotEmpty();
            assertThat(searchResponseDtoResponseEntity.getBody().getErrorCode()).isEqualTo(BizExceptionType.DATA_NOT_FONUD.getCode());
        }
    }

    @Test
    public void paymentSearchTest() throws Exception {
        // 결제정보 저장 후 조회하
        //given
        PaymentRequestDto paymentRequestDto = this.validPaymentRequestDto16;
        String saveUrl = "http://localhost:" + port + "/api/v1/payment";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PaymentRequestDto> httpEntity = new HttpEntity<>(paymentRequestDto, httpHeaders);

        // when
        ResponseEntity<PaymentResponseDto> paymentResponseDtoResponseEntity = restTemplate.exchange(saveUrl, HttpMethod.PUT, httpEntity, PaymentResponseDto.class);

        String managementId = paymentResponseDtoResponseEntity.getBody().getManagementId();

        // when
        String searchUrl = "http://localhost:" + port + "/api/v1/search/"+managementId;
        ResponseEntity<SearchResponseDto> searchResponseDtoResponseEntity = restTemplate.getForEntity(searchUrl, SearchResponseDto.class);

        // then
        assertThat(searchResponseDtoResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(searchResponseDtoResponseEntity.getBody().getManagementId().length()).isEqualTo(20);
        assertThat(searchResponseDtoResponseEntity.getBody().getTransactionTime()).isNotNull();
    }

    @Test
    public void paymentCancelTest() throws Exception {
        //given
        PaymentRequestDto paymentRequestDto = this.validPaymentRequestDto16;
        String saveUrl = "http://localhost:" + port + "/api/v1/payment";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PaymentRequestDto> httpEntity = new HttpEntity<>(paymentRequestDto, httpHeaders);

        ResponseEntity<PaymentResponseDto> paymentResponseDtoResponseEntity = restTemplate.exchange(saveUrl, HttpMethod.PUT, httpEntity, PaymentResponseDto.class);
        String managementId = paymentResponseDtoResponseEntity.getBody().getManagementId();

        String searchUrl = "http://localhost:" + port + "/api/v1/search/"+managementId;
        ResponseEntity<SearchResponseDto> searchResponseDtoResponseEntity = restTemplate.getForEntity(searchUrl, SearchResponseDto.class);

        // when
        String canelUrl = "http://localhost:" + port + "/api/v1/cancel";
        CancelReqeustDto cancelReqeustDto = CancelReqeustDto.builder()
                .managementId(managementId)
                .amount(paymentRequestDto.getAmount())
                .vat(paymentRequestDto.getVat())
                .build();

        ResponseEntity<CancelResponseDto> cancelResponseDtoResponseEntity = restTemplate.postForEntity(canelUrl, cancelReqeustDto, CancelResponseDto.class);
        String cancelManagementId = cancelResponseDtoResponseEntity.getBody().getManagementId();

        String cancelSearchUrl = "http://localhost:" + port + "/api/v1/search/"+cancelManagementId;
        ResponseEntity<SearchResponseDto> cancelSearchResponseDtoResponseEntity = restTemplate.getForEntity(cancelSearchUrl, SearchResponseDto.class);

        // then
        assertThat(cancelResponseDtoResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(cancelResponseDtoResponseEntity.getBody().getManagementId().length()).isEqualTo(20);
        assertThat(cancelResponseDtoResponseEntity.getBody().getManagementId()).isNotEqualTo(managementId);
        assertThat(cancelResponseDtoResponseEntity.getBody().getTransactionTime()).isNotNull();

        assertThat(cancelSearchResponseDtoResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(cancelSearchResponseDtoResponseEntity.getBody().getManagementId().length()).isEqualTo(20);
        assertThat(cancelSearchResponseDtoResponseEntity.getBody().getManagementId()).isNotEqualTo(managementId);
        assertThat(cancelSearchResponseDtoResponseEntity.getBody().getTransactionTime()).isNotNull();
        assertThat(cancelSearchResponseDtoResponseEntity.getBody().getPaymentType()).isEqualTo(PaymentType.CANCEL.getTypeKorName());
    }

}
