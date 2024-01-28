package pl.stosik.account.transferer

import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import pl.stosik.account.transferer.domain.AccountService
import pl.stosik.account.transferer.infrastracture.persistence.AccountRepository
import pl.stosik.account.transferer.infrastracture.persistence.memory.InMemoryAccountRepository
import pl.stosik.account.transferer.model.Money
import pl.stosik.account.transferer.model.account.Account
import pl.stosik.account.transferer.model.account.AccountId
import java.math.BigDecimal

internal class AccountServiceTest {

    private val accountRepository: AccountRepository = InMemoryAccountRepository()
    private val accountService = AccountService(accountRepository)

    @Test
    fun `should create account`() {
        // given
        val account = Account(AccountId.of(1L), Money.of(BigDecimal.ONE, "EUR"))

        // when
        val createdAccount = accountService.create(account)

        // then
        account shouldBe createdAccount
    }

    @Test
    fun `should get account by id`() {
        // given
        val account = Account(AccountId.of(1L), Money.of(BigDecimal.ONE, "EUR"))
        accountService.create(account)

        // when
        val foundAccount = accountService.findById(account.id)

        // then
        foundAccount shouldBe account
    }

    @Test
    fun `should compare account's funds`() {
        // given
        val account = Account(AccountId.of(1L), Money.of(BigDecimal.ONE, "EUR"))

        // and
        val secondAccount = Account(AccountId.of(1L), Money.of(BigDecimal.ONE, "EUR"))

        // when
        account shouldBeLessThan secondAccount
        secondAccount shouldBeGreaterThan account
    }
}