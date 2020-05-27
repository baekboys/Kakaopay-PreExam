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
public class SearchReqeustDto implements RequestDto  {
    // 관리번호
    private String managementId;

    @Override
    public boolean isValidate() {

        // 관리번호가 비어있거나 길이가 20이 아니면 유효값이 아님
        if( StringUtils.isEmpty(this.managementId) || this.managementId.length() != 20  ) {
            return false;
        }

        return true;
    }
}
