package com.racket.api.shared.payment

import com.racket.share.domain.payment.AccountPaymentRepository
import com.racket.share.domain.payment.exception.InvalidAccountPaymentException
import org.springframework.stereotype.Component

@Component
class BasePaymentComponent(
    private val paymentRepository: AccountPaymentRepository
) {
    fun validateAccount(accountId: Long) {
        val account = this.paymentRepository.findById(accountId)
            .orElseThrow { InvalidAccountPaymentException("Not Found Account") }
        if (!account.isPayable) {
            throw InvalidAccountPaymentException("Invalid Status Account")
        }
    }
}