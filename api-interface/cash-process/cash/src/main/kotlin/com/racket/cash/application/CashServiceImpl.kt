package com.racket.cash.application

import com.racket.cash.application.events.ChargingProduceEventVO
import com.racket.cash.entity.CashBalance
import com.racket.cash.entity.CashTransaction
import com.racket.cash.enums.CashEventType
import com.racket.cash.enums.CashTransactionStatusType
import com.racket.cash.exception.CashTransactionInsertException
import com.racket.cash.presentation.response.CashBalanceResponseView
import com.racket.cash.presentation.response.CashTransactionResponseView
import com.racket.cash.presentation.response.ChargeResponseView
import com.racket.cash.presentation.response.WithdrawAccountResponseView
import com.racket.cash.repository.AccountPaymentRepository
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
    val accountPaymentRepository: AccountPaymentRepository,
    val eventPublisher: ApplicationEventPublisher

) : CashService {

    private val log = KotlinLogging.logger { }

    @Transactional
    override fun charge(chargeDTO: CashService.ChargeDTO): ChargeResponseView {
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
            accountNo = chargeDTO.accountId,
            status = CashTransactionStatusType.ING
        )

    override fun getBalanceByUserId(userId: Long): CashBalanceResponseView {
        val cashBalance = this.cashBalanceRepository.findById(userId).orElseGet { CashBalance(userId = userId, balance = 0) }
        return CashBalanceResponseView(
            userId = userId,
            balance = cashBalance.balance
        )
    }

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

    override fun getTransactionById(transactionId: ObjectId): CashTransactionResponseView {
        val transaction = this.cashTransactionRepository.findById(transactionId).get()
        return CashTransactionResponseView(
            id = transaction.id.toString(),
            userId = transaction.userId,
            amount = transaction.amount,
            createdAt = transaction.id!!.date
        )
    }

    override fun updateBalance(userId: Long, amount: Long): CashBalanceResponseView {
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

    override fun getChargeUnitSet(): Set<Long> = setOf(50_000, 100_000, 150_000)

}