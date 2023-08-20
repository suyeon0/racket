package com.racket.api.payment.account

import com.racket.api.payment.account.response.WithdrawAccountResponseView
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/payment/account")
class AccountPaymentController(
    private val accountPaymentService: AccountPaymentService
) {

    @GetMapping("/withdrawAccountList/{userId}")
    fun getWithdrawAccountList(@PathVariable userId: Long): ResponseEntity<List<WithdrawAccountResponseView>> =
        ResponseEntity.ok()
            .body(this.accountPaymentService.getWithdrawAccountListByUserId(userId = userId))
}