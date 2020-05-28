package com.kakaopay.card.web.dto;

import com.kakaopay.card.Exception.BizException;

public interface RequestDto {

    // 입력한 값들의 정합성 체크
    boolean isValid() throws BizException;
}
