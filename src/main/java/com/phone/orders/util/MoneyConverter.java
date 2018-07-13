package com.phone.orders.util;

import java.math.BigDecimal;
import java.util.Currency;

public class MoneyConverter {

    private static final String EURO = "EUR";

    public static int toEuroCents(final String euros) {
        return toCents(euros, EURO);
    }

    public static String toEuro(final int amount) {
        return toEuro(amount, EURO);
    }

    public static String toEuro(final int amount, final String currency) {
        final int fractionDigits = Currency.getInstance(currency).getDefaultFractionDigits();
        return new BigDecimal(amount).movePointLeft(fractionDigits).stripTrailingZeros().toPlainString();
    }

    public static int toCents(final String amount, final String currency) {
        final int fractionDigits = Currency.getInstance(currency).getDefaultFractionDigits();
        return new BigDecimal(amount).movePointRight(fractionDigits).intValue();
    }

}
