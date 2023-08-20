package com.racket.api.payment.account

import com.racket.api.payment.account.response.WithdrawAccountResponseView

interface AccountPaymentService {

    fun getWithdrawAccountListByUserId(userId: Long): List<WithdrawAccountResponseView>
}