package com.racket.cash.consume.service.consume

import com.racket.api.payment.presentation.RetryPaymentCallRequiredException
import com.racket.cash.consume.client.CashFeignClient
import com.racket.cash.consume.service.PaymentCallService
import com.racket.cash.exception.ChargePayException
import com.racket.cash.exception.InvalidChargingTransactionException
import com.racket.cash.exception.UpdateDataAsChargingCompletedException
import com.racket.cash.presentation.request.CompleteCashChargeCommand
import com.racket.cash.presentation.response.CashTransactionResponseView
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.stereotype.Service

@Service
class CashConsumerServiceImpl(

    val paymentCallService: PaymentCallService,
    val cashClient: CashFeignClient,

    ): CashConsumerService {

    private val log = KotlinLogging.logger { }

    @KafkaListener(
        topics = ["CHARGING"], groupId = "CHARGING"
    )
    @RetryableTopic(
        autoCreateTopics = "false",
        include = [RetryPaymentCallRequiredException::class])
    override fun consumeChargingProcess(message: String) {
        log.info { "=============================== Charging Process Consume Start! =============================== " }
        log.info { "1. [Cash API 호출] get Temp Transaction Data From MongoDB" }
        val chargeRequestTransactionData = this.cashClient.getTransaction(transactionId = message).body
            ?: throw ChargePayException("=========== 임시 트랜잭션 데이터를 가져오지 못했습니다-${message}")

        val chargeAmount: Long = chargeRequestTransactionData.amount
        val userId: Long = chargeRequestTransactionData.userId
        log.info { "=========== Get 결과 : userId-${userId}, ${chargeAmount}원 요청" }

        this.validateTransactionData(chargeRequestTransactionData)
        log.info { "2. validate Passed." }

        log.info { "3. [Payment API 호출] - 결제" }
        this.payProcess(accountId = chargeRequestTransactionData.accountId, amount = chargeAmount)

        log.info { "4. [Cash API 호출] 충전 결과 DB 반영" }
        try {
            val balance = this.callCashApiToSaveData(chargeRequestTransactionData).body!!.balance
            log.info { "=========== 충전 완료 DB 반영 성공 -> 잔액: ${balance} 원" }
        } catch (e: Exception) {
            log.info { "=========== 충전 완료 DB 반영 실패" }
            throw UpdateDataAsChargingCompletedException()
        }
        log.info { "=============================== Charging Process Consume Done! ===============================" }
    }

    // 결제 API 호출 하기 전 validation
    private fun validateTransactionData(cashTransactionData: CashTransactionResponseView) {
        val cashTransaction = makeCashTransactionResponseToEntity(responseView = cashTransactionData)
        if (cashTransaction.isImpossibleTransactionToCharge()) {
            throw InvalidChargingTransactionException()
        }
    }


    // 결제 API 호출
    private fun payProcess(accountId: Long, amount:Long){
        log.info { "=========== 충전 결제 모듈 연동 시작" }
        this.paymentCallService.call(accountId = accountId, amount = amount)
    }

    // 충전 결과 DB 반영을 위한 Cash Api 호출
    private fun callCashApiToSaveData(chargeRequestTransactionData: CashTransactionResponseView) =
        this.cashClient.completeCharge(CompleteCashChargeCommand(
            userId = chargeRequestTransactionData.userId,
            amount = chargeRequestTransactionData.amount,
            transactionId = chargeRequestTransactionData.transactionId,
            accountId = chargeRequestTransactionData.accountId,
            eventType = chargeRequestTransactionData.eventType
        ))

}