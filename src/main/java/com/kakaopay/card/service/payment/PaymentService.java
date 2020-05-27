package com.kakaopay.card.service.payment;

import com.kakaopay.card.Exception.BizException;
import com.kakaopay.card.Exception.BizExceptionType;
import com.kakaopay.card.common.CalcUtil;
import com.kakaopay.card.common.ManagementIdGenerator;

import com.kakaopay.card.common.MaskingUtil;
import com.kakaopay.card.common.encrypt.KISASeedNumericEncryptor;
import com.kakaopay.card.domain.CardInfoDataMaker;
import com.kakaopay.card.domain.PaymentType;
import com.kakaopay.card.domain.cardinfo.CardInfo;
import com.kakaopay.card.domain.cardinfo.CardInfoRepository;
import com.kakaopay.card.domain.payment.Payment;
import com.kakaopay.card.domain.payment.PaymentRepository;
import com.kakaopay.card.web.dto.PaymentRequestDto;
import com.kakaopay.card.web.dto.PaymentResponseDto;
import com.kakaopay.card.web.dto.SearchReqeustDto;
import com.kakaopay.card.web.dto.SearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Service
public class PaymentService {
    // 결제정보 레파지토리
    private final PaymentRepository paymentRepository;

    // 카드사정보 레파지토리
    private final CardInfoRepository cardInfoRepository;

    @Transactional
    public PaymentResponseDto save(PaymentRequestDto requestDto) {
        //---------------------------------------------------------------------------
        // Request로 부터 전달받은 결제정보
        //---------------------------------------------------------------------------
        // PaymentRequestDto 값 세팅
        String cardnum = requestDto.getCardnum();
        String expired = requestDto.getExpired();
        String cvc = requestDto.getCvc();
        String installment  = requestDto.getInstallment();
        String amount = requestDto.getAmount();

        // vat가 비어있으면 자동계산
        String vat = requestDto.getVat();
        if(StringUtils.isEmpty(vat)) {
            vat = CalcUtil.getVatFromAmount(amount);
        }

        //---------------------------------------------------------------------------
        // 결제정보 저장 프로세스
        //---------------------------------------------------------------------------
        // 결제이므로 결제타입은 결제(PAYMENT)
        PaymentType paymentType = PaymentType.PAYMENT;

        // 결제타입이 결제(PAYMENT)이면서 유니크한 카드번호로 부터 결제관리번호 20자리 채번
        String paymentId = ManagementIdGenerator.getManagementIdByPaymentTypeAndCardNum(paymentType, cardnum);

        // 결제건이므로 취소관리번호는 빈값
        String cancelId = "";

        // 카드번호, 유효기간, cvc는 암호화
        String encCardnum = KISASeedNumericEncryptor.encrypt(cardnum);
        String encExpired = KISASeedNumericEncryptor.encrypt(expired);
        String encCvc = KISASeedNumericEncryptor.encrypt(cvc);

        // 결제정보를 저장 할 Payment 객체 생성
        Payment newPayment = Payment.builder()
                .paymentType(paymentType) // 결제타입 : 결제 PAYMENT
                .paymentId(paymentId)
                .cancelId(cancelId)
                .cardnum(encCardnum) // 암호화된 카드번호
                .expired(encExpired) // 암호화된 유효기간
                .cvc(encCvc) // 암호화된 CVC
                .amount(amount)
                .installment(installment)
                .vat(vat)
                .build()
                ;

        // 결제정보 저장
        Payment savePayment = paymentRepository.save(newPayment);

        //---------------------------------------------------------------------------
        // 카드사 정보 저장 프로세스
        //---------------------------------------------------------------------------
        // cardInfoPayment 객체로부터 카드사 정보 문자열 생성
        String cardinfoData = CardInfoDataMaker.getCardInfoDataByPayment(savePayment);

        // 관리번호와 카드사 정보를 저장할 CardInfo 객체 생성
        CardInfo newCardInfo = CardInfo
                .builder()
                .managementId(paymentId)
                .data(cardinfoData)
                .build();

        // 카드사 정보 저장
        CardInfo saveCardInfo = cardInfoRepository.save(newCardInfo);

        //---------------------------------------------------------------------------
        // Response 객체 생성
        //---------------------------------------------------------------------------
        PaymentResponseDto paymentResponseDto = PaymentResponseDto.builder()
                .managementId(savePayment.getPaymentId())
                .transactionTime(savePayment.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        return paymentResponseDto;
    }

    @Transactional(readOnly = true)
    public SearchResponseDto search(SearchReqeustDto requestDto) throws BizException {
        //---------------------------------------------------------------------------
        // Request로 부터 전달받은 관리번호
        //---------------------------------------------------------------------------
        String managementID = requestDto.getManagementId();

        //---------------------------------------------------------------------------
        // 관리번호값에 해당되는 결제/취소 정보 조회
        //---------------------------------------------------------------------------
        // (1) 취소관리ID에 해당되는 데이터가 있는지 조회 후
        Payment searchPayment = paymentRepository.findByCancelId(managementID)
                // (2) 없으면 결제관리번호ID에 해당되는 데이터 조회
                .orElseGet(
                        // (3) 없으면 BizException 발생
                        () -> paymentRepository.findByPaymentId(managementID).orElseThrow( () -> new BizException(BizExceptionType.DATA_NOT_FOND) )
                    )
                ;

        //---------------------------------------------------------------------------
        // 조회된 결제/취소 정보 값 세팅
        //---------------------------------------------------------------------------
        String cardNum = MaskingUtil.getMaskingCardNum(KISASeedNumericEncryptor.decrypt(searchPayment.getCardnum()));
        String expired = KISASeedNumericEncryptor.decrypt(searchPayment.getExpired());
        String cvc = KISASeedNumericEncryptor.decrypt(searchPayment.getCvc());
        String paymentType = searchPayment.getPaymentType().getTypeKorName();
        String amount = Long.toString(searchPayment.getAmount());
        String vat = Long.toString(searchPayment.getVat());
        String transactionTime = searchPayment.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        //---------------------------------------------------------------------------
        // Response 객체 생성
        //---------------------------------------------------------------------------
        SearchResponseDto searchResponseDto = SearchResponseDto.builder()
                .managementId(managementID)
                .cardNum(cardNum)
                .expired(expired)
                .cvc(cvc)
                .paymentType(paymentType)
                .amount(amount)
                .vat(vat)
                .transactionTime(transactionTime)
                .build()
                ;
        return searchResponseDto;
    }
}
