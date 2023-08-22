package com.racket.cash

import com.racket.cash.events.ChargingProduceEventVO
import com.racket.cash.entity.CashBalance
import com.racket.cash.exception.UpdateDataAsChargingCompletedException
import com.racket.cash.response.CashBalanceResponseView
import com.racket.cash.response.ChargeResponseView
import com.racket.cash.repository.CashBalanceRepository
import com.racket.cash.vo.ChargeVO
import mu.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CashServiceImpl(
    private val cashTransactionLogService: CashTransactionLogService,
    private val cashBalanceRepository: CashBalanceRepository,
    private val eventPublisher: ApplicationEventPublisher
) : CashService {

    private val log = KotlinLogging.logger { }
    companion object {
        val tempChargeUnitSet: Set<Long> = setOf(50_000, 100_000, 150_000)
    }

    @Transactional
    override fun requestCharge(chargeVO: ChargeVO): ChargeResponseView {
        val savedEvent = this.cashTransactionLogService.insertChargeTransaction(chargeVO)
        this.eventPublisher.publishEvent(ChargingProduceEventVO(transactionId = savedEvent.transactionId))

        return ChargeResponseView(
            id = savedEvent.id!!,
            amount = savedEvent.amount,
            userId = savedEvent.userId
        )
    }

    override fun getBalanceByUserId(userId: Long): CashBalanceResponseView {
        val cashBalance = this.cashBalanceRepository.findById(userId).orElseGet { CashBalance(userId = userId, balance = 0) }
        return CashBalanceResponseView(
            userId = userId,
            balance = cashBalance.balance
        )
    }

    @Transactional
    override fun completeCharge(chargeVO: ChargeVO): CashBalanceResponseView {
        this.cashTransactionLogService.insertChargeTransaction(chargeVO = chargeVO)

        return try {
            this.updateBalance(userId = chargeVO.userId, amount = chargeVO.amount)
        } catch (e: Exception) {
            log.error { e }
            throw UpdateDataAsChargingCompletedException()
        }
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

    override fun getChargeUnitSet() = tempChargeUnitSet
}