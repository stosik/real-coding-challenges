package pl.stosik.account.transferer.model.account

@JvmInline
value class AccountId private constructor(private val value: Long) {

    fun asUuid() = value

    companion object {
        fun of(id: Long) = AccountId(id)
    }
}
