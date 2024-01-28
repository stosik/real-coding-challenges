package pl.stosik.account.transferer.domain

import pl.stosik.account.transferer.infrastracture.persistence.AccountRepository
import pl.stosik.account.transferer.model.account.Account
import pl.stosik.account.transferer.model.account.AccountId

class AccountService(private val accountRepository: AccountRepository) {

    fun create(account: Account): Account {
        return accountRepository.save(account)
    }

    fun update(account: Account): Account? {
        return accountRepository.update(account)
    }

    fun findById(id: AccountId): Account? {
        return accountRepository.findById(id)
    }
}
