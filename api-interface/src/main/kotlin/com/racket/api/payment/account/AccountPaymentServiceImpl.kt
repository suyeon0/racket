package com.racket.api.payment.account

import com.racket.api.payment.account.response.WithdrawAccountResponseView
import com.racket.share.domain.payment.AccountPaymentRepository
import org.springframework.stereotype.Service

@Service
class AccountPaymentServiceImpl(

    private val accountPaymentRepository: AccountPaymentRepository

): AccountPaymentService {
    // 사용자가 저장한 계좌 리스트 중 결제 가능한 목록만 출력
    override fun getWithdrawAccountListByUserId(userId: Long): List<WithdrawAccountResponseView> {
        val result = this.accountPaymentRepository.findAllByUserIdOrderById(userId = userId).get()
        return result.stream()
            .filter { account -> account.isPayable }
            .map { account ->
                WithdrawAccountResponseView(
                    id = account.id,
                    bankCode = account.bankCode,
                    accountNumber = account.accountNumber,
                    isPayable = account.isPayable
                )
            }.toList()
    }
}