package pl.stosik.account.transferer.model

import pl.stosik.account.transferer.model.account.Account

data class MoneyTransferred(val debitedAccount: Account, val creditedAccount: Account)