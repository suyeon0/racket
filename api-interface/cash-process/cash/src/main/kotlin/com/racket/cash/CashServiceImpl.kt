package com.racket.cash

import com.racket.cash.events.ChargingProduceEventVO
import com.racket.cash.entity.CashBalance
import com.racket.cash.entity.CashTransaction
import com.racket.cash.enums.CashEventType
import com.racket.cash.enums.CashTransactionStatusType
import com.racket.cash.exception.CashTransactionInsertException
import com.racket.cash.exception.UpdateDataAsChargingCompletedException
import com.racket.cash.response.CashBalanceResponseView
import com.racket.cash.response.CashTransactionResponseView
import com.racket.cash.response.ChargeResponseView
import com.racket.api.payment.account.response.WithdrawAccountResponseView
import com.racket.api.payment.account.domain.AccountPaymentRepository
import com.racket.cash.repository.CashBalanceRepository
import com.racket.cash.repository.CashTransactionRepository
import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CashServiceImpl(
    val cashTransactionRepository: CashTransactionRepository,
    val cashBalanceRepository: CashBalanceRepository,
    val eventPublisher: ApplicationEventPublisher

) : CashService {

    private val log = KotlinLogging.logger { }
    companion object {
        val tempChargeUnitSet: Set<Long> = setOf(50_000, 100_000, 150_000)
    }

    @Transactional
    override fun requestCharge(chargeDTO: CashService.ChargeDTO): ChargeResponseView {
        try {
            val transaction = this.cashTransactionRepository.save(this.createCashTransactionEntity(chargeDTO))
            this.publishAsyncChargingProcessorEvent(eventId = transaction.id!!)

            return ChargeResponseView(
                id = transaction.id!!,
                amount = transaction.amount,
                userId = transaction.userId
            )
        } catch (e: Exception) {
            log.error { e }
            throw CashTransactionInsertException()
        }
    }

    private fun publishAsyncChargingProcessorEvent(eventId: ObjectId) =
        this.eventPublisher.publishEvent(ChargingProduceEventVO(eventId = eventId))

    private fun createCashTransactionEntity(chargeDTO: CashService.ChargeDTO) =
        CashTransaction(
            transactionId = ObjectId(),
            userId = chargeDTO.userId,
            amount = chargeDTO.amount,
            eventType = CashEventType.CHARGING,
            accountId = chargeDTO.accountId,
            status = CashTransactionStatusType.REQUEST
        )

    override fun getBalanceByUserId(userId: Long): CashBalanceResponseView {
        val cashBalance = this.cashBalanceRepository.findById(userId).orElseGet { CashBalance(userId = userId, balance = 0) }
        return CashBalanceResponseView(
            userId = userId,
            balance = cashBalance.balance
        )
    }

    override fun getTransactionById(transactionId: ObjectId): CashTransactionResponseView {
        val transaction = this.cashTransactionRepository.findById(transactionId).get()
        return CashTransactionResponseView(
            id = transaction.id.toString(),
            userId = transaction.userId,
            amount = transaction.amount,
            createdAt = transaction.id!!.date,
            transactionId = transaction.transactionId.toString(),
            accountId = transaction.accountId,
            status = transaction.status,
            eventType = transaction.eventType
        )
    }

    @Transactional
    override fun completeCharge(chargeDTO: CashService.ChargeDTO): CashBalanceResponseView {
        try {
            val updateBalanceResult = updateBalance(userId = chargeDTO.userId, amount = chargeDTO.amount)

            this.saveChargingCompletedTransaction(chargeDTO)
            return updateBalanceResult
        } catch (e: Exception) {
            throw UpdateDataAsChargingCompletedException()
        }
    }

    // 트랜잭션 완료 로그 저장
    private fun saveChargingCompletedTransaction(chargeDTO: CashService.ChargeDTO): CashTransaction {
        val savedData = this.cashTransactionRepository.save(this.createCashTransactionEntity(chargeDTO))
        log.info { "=========== 트랜잭션 완료 ID-${savedData.id}" }
        return savedData
    }

    // 캐시 잔액 반영
    private fun updateBalance(userId: Long, amount: Long): CashBalanceResponseView {
        val balanceOpt = this.cashBalanceRepository.findById(userId)
        return if (balanceOpt.isPresent) {
            this.cashBalanceRepository.save(balanceOpt.get().updateBalance(amount = amount))
            CashBalanceResponseView(
                userId = userId,
                balance = this.cashBalanceRepository.findById(userId).get().balance
            )
        } else {
            this.cashBalanceRepository.save(CashBalance(userId = userId, balance = amount))
            CashBalanceResponseView(
                userId = userId,
                balance = amount
            )
        }
    }

    override fun getChargeUnitSet(): Set<Long> = tempChargeUnitSet
}