package com.kakaopay.card.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.Callable;

public class CalcUtil {

    // vat : amount / 11
    private static final BigDecimal divideNum = new BigDecimal("11");

    public static String getVatFromAmount(String amount) {

        BigDecimal inputBigDeciaml = new BigDecimal(amount);

        BigDecimal calcResult = inputBigDeciaml.divide(divideNum, 0, BigDecimal.ROUND_HALF_UP);

        String vat = calcResult.toPlainString();

        return vat;
    }
}
