package pl.stosik.account.transferer.domain

import pl.stosik.account.transferer.model.Money
import pl.stosik.account.transferer.model.MoneyTransferred
import pl.stosik.account.transferer.model.account.Account
import pl.stosik.account.transferer.model.account.AccountId


class TransferService(private val accountService: AccountService) {

    fun withdraw(accountId: AccountId, amount: Money): Account {
        val account = accountService.findById(accountId)
        requireNotNull(account) { "Account with id $accountId does not exist" }

        return synchronized(account) {
            val updatedAccount = account.subtractFunds(amount)
            accountService.update(updatedAccount)
            updatedAccount
        }
    }

    fun deposit(accountId: AccountId, amount: Money): Account {
        val account = accountService.findById(accountId)
        requireNotNull(account) { "Account with id $accountId does not exist" }

        return synchronized(account) {
            val updatedAccount = account.addFunds(amount)
            accountService.update(updatedAccount)
            updatedAccount
        }
    }

    fun transferBetween(fromAccountId: AccountId, toAccountId: AccountId, amount: Money): MoneyTransferred {
        val fromAccount = accountService.findById(fromAccountId)
        val toAccount = accountService.findById(toAccountId)

        requireNotNull(fromAccount) { "Account with id $fromAccount does not exist" }
        requireNotNull(toAccount) { "Account with id $toAccount does not exist" }

        val from = if (fromAccount >= toAccount) fromAccount else toAccount
        val to = if (fromAccount < toAccount) fromAccount else toAccount

        return synchronized(from) {
            synchronized(to) {
                val debtor = fromAccount.subtractFunds(amount)
                val creditor = toAccount.addFunds(amount)
                accountService.update(debtor)
                accountService.update(creditor)

                MoneyTransferred(debitedAccount = debtor, creditedAccount = creditor)
            }
        }
    }
}
