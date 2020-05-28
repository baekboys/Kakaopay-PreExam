package com.kakaopay.card.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelReqeustDto implements RequestDto  {
    // 관리번호
    private String managementId;
    // 취소금액
    private String amount;
    // 취소부가가치세
    private String vat;

    @Override
    public boolean isValid() {

        // 관리번호가 비어있거나 길이가 20이 아니면 유효값이 아님
        if( StringUtils.isEmpty(this.managementId) || this.managementId.length() != 20  ) {
            return false;
        }

        // 취소금액 체크
        if( StringUtils.isEmpty(amount) || !StringUtils.isNumeric(amount) || !( Long.valueOf(amount)  >= 100L && Long.valueOf(amount) <= 1000000000L ) ) {
            return false;
        }

        // 부가가치세 체크
        if( !StringUtils.isEmpty(vat) && ( vat.length() > 10 || ! StringUtils.isNumeric(vat) ) ) {
            return false;
        }

        // 금액보다 부가가치세가 큰지 체크
        if( !StringUtils.isEmpty(amount) && !StringUtils.isEmpty(vat) ) {
            long requestAmount = Long.valueOf(amount);
            long requestVat = Long.valueOf(vat);

            if(requestVat > requestAmount) {
                return false;
            }
        }

        return true;
    }
}
