package com.racket.cash.consume.service.consume

import com.racket.api.payment.presentation.RetryPaymentCallRequiredException
import com.racket.cash.consume.client.CashFeignClient
import com.racket.cash.consume.service.PaymentCallService
import com.racket.cash.exception.ChargePayException
import com.racket.cash.exception.InvalidChargingTransactionException
import com.racket.cash.exception.UpdateBalanceException
import com.racket.cash.presentation.request.UpdateBalanceCommand
import com.racket.cash.presentation.response.CashTransactionResponseView
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class CashConsumerServiceImpl(

    private val paymentCallService: PaymentCallService,
    private val cashClient: CashFeignClient

): CashConsumerService {

    private val log = KotlinLogging.logger { }

    @KafkaListener(
        topics = ["charging"], groupId = "racket"
    )
    @RetryableTopic(
        autoCreateTopics = "false",
        include = [RetryPaymentCallRequiredException::class])
    @Transactional
    override fun consumeChargingProcess(message: String) {

        log.info { "=============================== Charging Process Consume Start! =============================== " }
        log.info { "1. get Temp Transaction Data From MongoDB" }
        val cashTransactionData = this.cashClient.getTransaction(transactionId = message).body
            ?: throw ChargePayException("=========== 임시 트랜잭션 데이터를 가져오지 못했습니다-${message}")

        val chargeAmount: Long = cashTransactionData.amount
        val userId: Long = cashTransactionData.userId
        log.info { "=========== Get 결과 : userId-${userId}, ${chargeAmount}원 요청" }

        this.validateTransactionData(cashTransactionData)
        log.info { "2. validate Passed." }

        log.info { "3. 결제 API 호출" }
        this.payProcess(chargeAmount)

        log.info { "4. 충전 합계 테이블 반영" }
        this.updateBalance(userId = userId, amount = chargeAmount)

        log.info { "=============================== Charging Process Consume Done! ===============================" }

    }

    private fun validateTransactionData(cashTransactionData: CashTransactionResponseView) {
        val cashTransaction = makeCashTransactionResponseToEntity(responseView = cashTransactionData)
        if (cashTransaction.isImpossibleTransactionToCharge()) {
            throw InvalidChargingTransactionException()
        }
    }

    private fun updateBalance(userId: Long, amount: Long) {
        try {
            val result = this.cashClient.postToUpdateBalance(
                UpdateBalanceCommand(
                    userId = userId, amount = amount
                )
            )
            val balance = result.body!!.balance
            log.info { "=========== 충전 합계 테이블 반영 완료 -> 잔액: ${balance} 원" }
        } catch (e: Exception) {
            log.error { e.printStackTrace() }
            throw UpdateBalanceException()
        }
    }


    // 결제모듈 연동
    private fun payProcess(price: Long){
        log.info { "=========== 충전 결제 모듈 연동 시작" }
        this.paymentCallService.call(price)
    }
}