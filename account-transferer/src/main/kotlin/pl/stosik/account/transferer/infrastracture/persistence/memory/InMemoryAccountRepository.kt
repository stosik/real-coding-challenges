package pl.stosik.account.transferer.infrastracture.persistence.memory

import pl.stosik.account.transferer.infrastracture.persistence.AccountRepository
import pl.stosik.account.transferer.model.account.Account
import pl.stosik.account.transferer.model.account.AccountId
import java.util.concurrent.ConcurrentHashMap

class InMemoryAccountRepository : AccountRepository {

    private val accounts = ConcurrentHashMap<AccountId, Account>()

    override fun save(account: Account): Account {
        return accounts.putIfAbsent(account.id, account) ?: account
    }

    override fun update(account: Account): Account? {
        return accounts.computeIfPresent(account.id) { _, _ -> account }
    }

    override fun findById(id: AccountId): Account? {
        return accounts[id]
    }
}