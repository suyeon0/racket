package com.racket.cash.application

import com.racket.cash.application.events.ChargingProduceEventVO
import com.racket.cash.entity.CashBalance
import com.racket.cash.entity.CashTransaction
import com.racket.cash.entity.UserChargingWayInfo
import com.racket.cash.enums.CashEventType
import com.racket.cash.exception.CashTransactionInsertException
import com.racket.cash.exception.NotExistSavedChargeWayException
import com.racket.cash.presentation.response.CashBalanceResponseView
import com.racket.cash.presentation.response.CashTransactionResponseView
import com.racket.cash.presentation.response.ChargeResponseView
import com.racket.cash.repository.CashBalanceRepository
import com.racket.cash.repository.CashTransactionRepository
import com.racket.cash.repository.UserChargingWayInfoRepository
import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CashServiceImpl(
    val cashTransactionRepository: CashTransactionRepository,
    val cashBalanceRepository: CashBalanceRepository,
    val cashUserChargingWayInfoRepository: UserChargingWayInfoRepository,
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
            userId = chargeDTO.userId,
            amount = chargeDTO.amount,
            chargingWayId = chargeDTO.chargingWayId,
            eventType = CashEventType.CHARGING
        )

    private fun validateToCharge() {

    }

    override fun getBalanceByUserId(userId: Long): CashBalanceResponseView {
        val cashBalance = this.cashBalanceRepository.findById(userId).orElseGet { CashBalance(userId = userId, balance = 0) }
        return CashBalanceResponseView(
            userId = userId,
            balance = cashBalance.balance
        )
    }

    override fun getChargingWayById(userId: Long): UserChargingWayInfo =
        this.cashUserChargingWayInfoRepository.findById(userId).orElseThrow { NotExistSavedChargeWayException() }

    override fun getTransactionById(transactionId: ObjectId): CashTransactionResponseView {
        val transaction = this.cashTransactionRepository.findById(transactionId).get()
        val userChargingWayInfo = this.cashUserChargingWayInfoRepository.findById(transaction.chargingWayId).get()
        return CashTransactionResponseView(
            id = transaction.id.toString(),
            userId = transaction.userId,
            amount = transaction.amount,
            userChargingWayInfo = userChargingWayInfo,
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

}