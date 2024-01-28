package pl.stosik.account.transferer

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import pl.stosik.account.transferer.domain.AccountService
import pl.stosik.account.transferer.domain.TransferService
import pl.stosik.account.transferer.infrastracture.persistence.AccountRepository
import pl.stosik.account.transferer.infrastracture.persistence.memory.InMemoryAccountRepository
import pl.stosik.account.transferer.model.Money
import pl.stosik.account.transferer.model.MoneyTransferred
import pl.stosik.account.transferer.model.account.Account
import pl.stosik.account.transferer.model.account.AccountId
import java.math.BigDecimal

internal class TransferServiceTest {

    private val accountRepository: AccountRepository = InMemoryAccountRepository()
    private val accountService = AccountService(accountRepository)

    private val transferService = TransferService(accountService)

    @Test
    fun `should withdraw money from account`() {
        // given
        val account = Account(AccountId.of(1L), Money.of(BigDecimal.ONE, "EUR"))
        accountService.create(account)

        // when
        val result = transferService.withdraw(account.id, Money.of(BigDecimal.ONE, "EUR"))

        // then
        result shouldBe Account(account.id, Money.of(BigDecimal.ZERO, "EUR"))
    }

    @Test
    fun `should deposit money to account`() {
        // given
        val account = Account(AccountId.of((1L)), Money.of(BigDecimal.ONE, "EUR"))
        accountService.create(account)

        // when
        val result = transferService.deposit(account.id, Money.of(BigDecimal.ONE, "EUR"))

        // then
        result shouldBe Account(AccountId.of(1L), Money.of(BigDecimal.valueOf(2), "EUR"))
    }

    @Test
    fun `should transfer money between accounts`() {
        // given
        val accountA = Account(AccountId.of((1L)), Money.of(BigDecimal.ONE, "EUR"))
        val accountB = Account(AccountId.of(2L), Money.of(BigDecimal.ONE, "EUR"))
        accountService.create(accountA)
        accountService.create(accountB)

        // when
        val result = transferService.transferBetween(accountA.id, accountB.id, Money.of(BigDecimal.ONE, "EUR"))

        // then
        result shouldBe MoneyTransferred(
            debitedAccount = Account(accountA.id, Money.of(BigDecimal.ZERO, "EUR")),
            creditedAccount = Account(accountB.id, Money.of(BigDecimal.valueOf(2), "EUR")),
        )
    }
}