package pl.stosik.account.transferer.model.account

import pl.stosik.account.transferer.model.Money

data class Account(val id: AccountId, val money: Money) : Comparable<Account> {

    override fun compareTo(other: Account): Int {
        return id.asUuid().compareTo(other.id.asUuid())
    }

    fun addFunds(money: Money): Account {

        return this.copy(money = this.money + money)
    }

    fun subtractFunds(money: Money): Account {

        return this.copy(money = this.money - money)
    }
}