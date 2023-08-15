package com.racket.cash.consume.service.consume

import com.racket.api.payment.presentation.RetryPaymentCallRequiredException
import com.racket.cash.consume.client.CashFeignClient
import com.racket.cash.consume.service.PaymentCallService
import com.racket.cash.entity.CashTransaction
import com.racket.cash.enums.CashEventType
import com.racket.cash.enums.CashTransactionStatusType
import com.racket.cash.exception.ChargePayException
import com.racket.cash.exception.InvalidChargingTransactionException
import com.racket.cash.exception.UpdateBalanceException
import com.racket.cash.presentation.request.UpdateBalanceCommand
import com.racket.cash.presentation.response.CashTransactionResponseView
import com.racket.cash.repository.CashTransactionRepository
import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class CashConsumerServiceImpl(

    val paymentCallService: PaymentCallService,
    val cashClient: CashFeignClient,
    val cashTransactionRepository: CashTransactionRepository

    ): CashConsumerService {

    private val log = KotlinLogging.logger { }

    @KafkaListener(
        topics = ["CHARGING"], groupId = "CHARGING"
    )
    @RetryableTopic(
        autoCreateTopics = "false",
        include = [RetryPaymentCallRequiredException::class])
    @Transactional
    override fun consumeChargingProcess(message: String) {
        log.info { "=============================== Charging Process Consume Start! =============================== " }
        log.info { "1. get Temp Transaction Data From MongoDB" }
        val chargeRequestTransactionData = this.cashClient.getTransaction(transactionId = message).body
            ?: throw ChargePayException("=========== 임시 트랜잭션 데이터를 가져오지 못했습니다-${message}")

        val chargeAmount: Long = chargeRequestTransactionData.amount
        val userId: Long = chargeRequestTransactionData.userId
        log.info { "=========== Get 결과 : userId-${userId}, ${chargeAmount}원 요청" }

        this.validateTransactionData(chargeRequestTransactionData)
        log.info { "2. validate Passed." }

        log.info { "3. 결제 API 호출" }
        this.payProcess(chargeAmount)

        log.info { "4. 충전 합계 테이블 반영" }
        this.updateBalance(userId = userId, amount = chargeAmount)

        log.info { "5. 충전 트랜잭션 완료 로그 DB Insert"}
        val savedData = this.saveChargeTransactionCompletionData(chargeRequestTransactionData)
        log.info { "=========== 트랜잭션 완료 ID-${savedData.id}" }

        log.info { "=============================== Charging Process Consume Done! ===============================" }
    }

    private fun saveChargeTransactionCompletionData(responseView: CashTransactionResponseView): CashTransaction {
        val transactionEntityToSaveCompletedVersion = this.makeCashTransactionResponseViewToEntity(responseView)
        return this.cashTransactionRepository.save(transactionEntityToSaveCompletedVersion)
    }

    private fun makeCashTransactionResponseViewToEntity(responseView: CashTransactionResponseView) =
        CashTransaction(
            transactionId = ObjectId(responseView.transactionId),
            userId = responseView.userId,
            amount = responseView.amount,
            eventType = CashEventType.CHARGING,
            accountNo = responseView.accountNo,
            status = CashTransactionStatusType.COMPLETED
        )

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