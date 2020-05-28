package com.kakaopay.card.service.payment;

import com.kakaopay.card.Exception.*;
import com.kakaopay.card.common.CalcUtil;
import com.kakaopay.card.common.ManagementIdGenerator;
import com.kakaopay.card.common.MaskingUtil;
import com.kakaopay.card.common.encrypt.KISASeedEncryptor;
import com.kakaopay.card.domain.PaymentType;
import com.kakaopay.card.domain.cardinfo.CardInfo;
import com.kakaopay.card.domain.cardinfo.CardInfoDataBuider;
import com.kakaopay.card.domain.cardinfo.CardInfoRepository;
import com.kakaopay.card.domain.cardinfo.TransactionDto;
import com.kakaopay.card.domain.payment.Payment;
import com.kakaopay.card.domain.payment.PaymentRepository;
import com.kakaopay.card.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

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
        String encCardnum = "";
        String encExpired = "";
        String encCvc = "";

        try {
            encCardnum = KISASeedEncryptor.encrypt(cardnum);
            encExpired = KISASeedEncryptor.encrypt(expired);
            encCvc = KISASeedEncryptor.encrypt(cvc);
        } catch (Exception e) {
            throw new PaymentBizException(BizExceptionType.ENCRYPTOR_ENC_FAIL);
        }

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
        Payment savePayment = null;
        try {
            savePayment = paymentRepository.save(newPayment);
            if(savePayment == null) {
                throw new PaymentBizException(BizExceptionType.DATA_NOT_SAVE);
            }
        } catch (Exception e) {
            throw new PaymentBizException(BizExceptionType.DATA_NOT_SAVE);
        }

        //---------------------------------------------------------------------------
        // 카드사 정보 저장 프로세스
        //---------------------------------------------------------------------------
        // TransactionInfo 객체생성을 위한 암호화 된 정보값 세팅
        String encryptInfo = null;
        try {
            encryptInfo = KISASeedEncryptor.encryptCardInfo(cardnum, expired, cvc);
        } catch (Exception e) {
            throw new PaymentBizException(BizExceptionType.ENCRYPTOR_ENC_FAIL);
        }

        // TransactionInfo 객체 생성
        TransactionDto transactionDto = TransactionDto.builder()
                .managementId(paymentId)
                .oriManagementId("")
                .paymentType(paymentType.getTypeName())
                .cardnum(cardnum)
                .expired(expired)
                .cvc(cvc)
                .installment(installment)
                .amount(amount)
                .vat(vat)
                .encryptInfo(encryptInfo)
                .build()
                ;

        // TransactionInfo 객체로부터 카드사 정보 문자열 생성
        String cardinfoData = CardInfoDataBuider.getCardInfoDataByTransactionDto(transactionDto);

        // 관리번호와 카드사 정보를 저장할 CardInfo 객체 생성
        CardInfo newCardInfo = CardInfo
                .builder()
                .managementId(paymentId)
                .data(cardinfoData)
                .build();

        // 카드사 정보 저장
        CardInfo saveCardInfo = null;
        try {
            saveCardInfo = cardInfoRepository.save(newCardInfo);
            if(savePayment == null) {
                throw new PaymentBizException(BizExceptionType.DATA_NOT_SAVE);
            }
        } catch (Exception e) {
            throw new PaymentBizException(BizExceptionType.DATA_NOT_SAVE);
        }

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
    public SearchResponseDto search(String managementId) {

        //---------------------------------------------------------------------------
        // 관리번호값에 해당되는 결제/취소 정보 조회
        //---------------------------------------------------------------------------
        // (1) 취소관리ID에 해당되는 데이터가 있는지 조회 후
        Payment searchPayment = paymentRepository.findByCancelId(managementId)

                .orElseGet(
                        // (2) 없으면 결제관리번호ID와 결제에 해당되는 데이터 조회 !!! 결제인지 체크를 하지 않으면 취소건으로 다건 발생될 수 있음
                        () -> paymentRepository.findByPaymentIdAndPaymentType(managementId, PaymentType.PAYMENT).orElseThrow(
                                // (3) 없으면 BizException 발생
                                () -> new SearchBizException(BizExceptionType.DATA_NOT_FONUD)
                        )
                    )
                ;

        //---------------------------------------------------------------------------
        // 조회된 결제/취소 정보 값 세팅
        //---------------------------------------------------------------------------
        // 카드번호는 복호화 후 마스킹 처리
        String cardNum = MaskingUtil.getMaskingCardNum(KISASeedEncryptor.decrypt(searchPayment.getCardnum()));

        // 유효기간은 복호화
        String expired = "";
        // CVC는 복호화
        String cvc = "";
        try {
            expired = KISASeedEncryptor.decrypt(searchPayment.getExpired());
            cvc = KISASeedEncryptor.decrypt(searchPayment.getCvc());
        } catch (Exception e) {
            throw new SearchBizException(BizExceptionType.ENCRYPTOR_DEC_FAIL);
        }


        // 결제타입을 한글로 리턴
        String paymentType = searchPayment.getPaymentType().getTypeKorName();
        // 금액과 부가가치세는 String으로 변환
        String amount = Long.toString(searchPayment.getAmount());
        String vat = Long.toString(searchPayment.getVat());
        // 거래시간은 포맷 변경하여 String으로 변환
        String transactionTime = searchPayment.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        //---------------------------------------------------------------------------
        // Response 객체 생성
        //---------------------------------------------------------------------------
        SearchResponseDto searchResponseDto = SearchResponseDto.builder()
                .managementId(managementId)
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

    @Transactional
    public CancelResponseDto cancel(CancelReqeustDto cancelReqeustDto) {
        //---------------------------------------------------------------------------
        // Request로 부터 전달받은 취소정보
        //---------------------------------------------------------------------------
        String managementId = cancelReqeustDto.getManagementId();
        String amount = cancelReqeustDto.getAmount();
        String vat = cancelReqeustDto.getVat();

        //---------------------------------------------------------------------------
        // 관리번호값에 해당되는 결제 정보 조회
        //---------------------------------------------------------------------------
        // (1) 결제관리ID에 해당되는 데이터가 있는지 조회
        Payment searchPayment = paymentRepository.findByPaymentIdAndPaymentType(managementId, PaymentType.PAYMENT)
                // (2) 없으면 취소를 할수 없으므로 예외 발생
                .orElseThrow(
                        () -> new CancelBizException(BizExceptionType.DATA_PAYMENT_NOT_FONUD)
                )
                ;

        //---------------------------------------------------------------------------
        // 결제 정보로부터 취소건이 있는지 조회
        //---------------------------------------------------------------------------
        Optional<Payment> cancelPayment = paymentRepository.findByPaymentIdAndPaymentType(managementId, PaymentType.CANCEL);
        // 취소건이 있으면 더이상 진행 불가
        if(cancelPayment.isPresent()) {
            new CancelBizException(BizExceptionType.CANCEL_FINISHED);
        }

        //---------------------------------------------------------------------------
        // 취소 가능여부 체크
        //---------------------------------------------------------------------------
        // (1) 취소관련 데이터 조회
        // 취소금액
        Long cancelAmount = Long.valueOf(amount);
        // 취소 부가가치세 (만약 비어있으면 결제건의 부가가치세로 세팅)
        Long cancelVat = StringUtils.isEmpty(vat) ? searchPayment.getVat() : Long.valueOf(vat);
        // 결제금액
        Long paymentAmount = searchPayment.getAmount();
        // 결제 부가가치세
        Long paymentVat = searchPayment.getVat();

        // (2) 취소가능금액 체크
        if(cancelAmount == 0 || cancelAmount > paymentAmount) {
            throw new CancelBizException(BizExceptionType.CANCEL_NOT_AMOUCNT);
        }

        // (3) 취소가능 부가가치세 체크
        if(cancelVat > paymentVat) {
            throw new CancelBizException(BizExceptionType.CANCEL_NOT_VAT);
        }

        //---------------------------------------------------------------------------
        // 취소 데이터 세팅 후 취소데이터 생성
        //---------------------------------------------------------------------------
        // (1) 복호화 된 카드번호, 유효기간, CVC
        String decCardNum = "";
        String decExpired = "";
        String decCvc = "";
        try {
            decCardNum = KISASeedEncryptor.decrypt(searchPayment.getCardnum());
            decExpired = KISASeedEncryptor.decrypt(searchPayment.getExpired());
            decCvc = KISASeedEncryptor.decrypt(searchPayment.getCvc());
        } catch (Exception e) {
            throw new CancelBizException(BizExceptionType.ENCRYPTOR_DEC_FAIL);
        }

        // (2) 결제타입이 취소(PAYMENT)이면서 유니크한 카드번호로 부터 취소관리번호 20자리 채번
        PaymentType cancelPaymentType = PaymentType.CANCEL;
        String cancelId = ManagementIdGenerator.getManagementIdByPaymentTypeAndCardNum(cancelPaymentType, decCardNum);
        // (3) 할부는 00으로 세팅
        String cancelInstallment = "00";
        // (4) 그외의 데이터는 결제정보와 동일

        // 취소정보를 저장 할 Payment 객체 생성
        Payment cancelData = Payment.builder()
                .paymentType(cancelPaymentType) // 결제타입 : 결제 CANCEL
                .paymentId(managementId) // 결제관리ID(원 관리번호)
                .cancelId(cancelId) // 취소관리ID
                .cardnum(searchPayment.getCardnum()) // 암호화된 카드번호
                .expired(searchPayment.getExpired()) // 암호화된 유효기간
                .cvc(searchPayment.getCvc()) // 암호화된 CVC
                .amount(amount)
                .installment(cancelInstallment)
                .vat(vat)
                .build()
                ;
        // 취소정보 저장
        Payment cancelSave = null;
        try {
            cancelSave = paymentRepository.save(cancelData);
            if(cancelSave == null) {
                throw new CancelBizException(BizExceptionType.DATA_NOT_SAVE);
            }
        } catch (Exception e) {
            throw new CancelBizException(BizExceptionType.DATA_NOT_SAVE);
        }

        //---------------------------------------------------------------------------
        // 카드사 정보 저장 프로세스
        //---------------------------------------------------------------------------
        // TransactionInfo 객체생성을 위한 암호화 된 정보값 세팅
        String encryptInfo = "";
        try {
            encryptInfo = KISASeedEncryptor.encryptCardInfo(decCardNum, decExpired, decExpired);
        } catch (Exception e) {
            throw new CancelBizException(BizExceptionType.ENCRYPTOR_ENC_FAIL);
        }

        // TransactionInfo 객체 생성
        TransactionDto transactionDto = TransactionDto.builder()
                .managementId(cancelId)
                .oriManagementId(managementId)
                .paymentType(cancelPaymentType.getTypeName())
                .cardnum(decCardNum)
                .expired(decExpired)
                .cvc(decCvc)
                .installment(cancelInstallment)
                .amount(amount)
                .vat(vat)
                .encryptInfo(encryptInfo)
                .build()
                ;

        // TransactionInfo 객체로부터 카드사 정보 문자열 생성
        String cardinfoData = CardInfoDataBuider.getCardInfoDataByTransactionDto(transactionDto);

        // 관리번호와 카드사 정보를 저장할 CardInfo 객체 생성
        CardInfo cancelCardInfo = CardInfo
                .builder()
                .managementId(cancelId)
                .data(cardinfoData)
                .build();

        // 카드사 정보 저장
        CardInfo saveCardInfo = null;
        try {
            saveCardInfo = cardInfoRepository.save(cancelCardInfo);
            if(saveCardInfo == null) {
                throw new CancelBizException(BizExceptionType.DATA_NOT_SAVE);
            }
        } catch (Exception e) {
            throw new CancelBizException(BizExceptionType.DATA_NOT_SAVE);
        }

        //---------------------------------------------------------------------------
        // Response 객체 생성
        //---------------------------------------------------------------------------
        CancelResponseDto cancelResponseDto = CancelResponseDto.builder()
                .managementId(cancelSave.getCancelId())
                .transactionTime(cancelSave.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        return cancelResponseDto;
    }
}
