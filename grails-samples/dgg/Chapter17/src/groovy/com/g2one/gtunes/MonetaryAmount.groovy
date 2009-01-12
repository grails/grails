package com.g2one.gtunes

class MonetaryAmount implements Serializable {
    private final BigDecimal value
    private final Currency currency

    MonetaryAmount(value, Currency currency) {
        this.value = value.toBigDecimal()
        this.currency = currency
    }

    BigDecimal getValue() { this.value }
    Currency getCurrency() { this.currency }

    boolean equals(o) {
        if (!(o instanceof MonetaryAmount)) return false
        return o.value == this.value && o.currency == this.currency
    }

    int hashCode() {
        int result = 23468
        result += 37 * this.value.hashCode()
        result += 37 * this.currency.hashCode()
        return result
    }
}
