package pl.stosik.account.transferer.infrastracture.persistence

import pl.stosik.account.transferer.model.account.Account
import pl.stosik.account.transferer.model.account.AccountId

interface AccountRepository {

    fun save(account: Account): Account

    fun update(account: Account): Account?

    fun findById(id: AccountId): Account?
}