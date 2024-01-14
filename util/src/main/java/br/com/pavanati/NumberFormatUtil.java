package br.com.pavanati;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class NumberFormatUtil {

    public static String formatValuePtBr(BigDecimal paymentValue) {
        if (Objects.nonNull(paymentValue)) {
            NumberFormat nf = NumberFormat.getInstance(new Locale("pt", "BR"));
            nf.setMinimumFractionDigits(2);
            return nf.format(paymentValue);
        }
        return "";
    }

}