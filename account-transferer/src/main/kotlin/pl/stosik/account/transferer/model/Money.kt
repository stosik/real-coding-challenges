package pl.stosik.account.transferer.model

import java.math.BigDecimal

data class Money private constructor(val amount: BigDecimal, val currency: String) {

    operator fun plus(other: Money): Money {
        require(currency == other.currency) { "Incompatible currencies" }
        return Money(amount.add(other.amount), currency)
    }

    operator fun minus(other: Money): Money {
        require(currency == other.currency) { "Incompatible currencies" }
        return Money(amount.minus(other.amount), currency)
    }

    companion object {

        fun of(amount: BigDecimal, currency: String) = Money(amount, currency)
    }
}
