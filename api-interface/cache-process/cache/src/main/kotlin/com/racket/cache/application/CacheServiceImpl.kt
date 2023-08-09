package com.racket.cache.application

import com.racket.cache.CacheEventType
import com.racket.cache.entity.CacheBalance
import com.racket.cache.entity.CacheTransaction
import com.racket.cache.entity.UserChargingWayInfo
import com.racket.cache.application.events.ChargingProduceEventVO
import com.racket.cache.exception.CacheChargeException
import com.racket.cache.exception.NotExistSavedChargeWayException
import com.racket.cache.presentation.response.CacheBalanceResponseView
import com.racket.cache.presentation.response.CacheTransactionResponseView
import com.racket.cache.presentation.response.ChargeResponseView
import com.racket.cache.repository.CacheBalanceRepository
import com.racket.cache.repository.CacheTransactionRepository
import com.racket.cache.repository.UserChargingWayInfoRepository
import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CacheServiceImpl(
    val cacheTransactionRepository: CacheTransactionRepository,
    val cacheBalanceRepository: CacheBalanceRepository,
    val cacheUserChargingWayInfoRepository: UserChargingWayInfoRepository,
    val eventPublisher: ApplicationEventPublisher

) : CacheService {

    private val log = KotlinLogging.logger { }

    @Transactional
    override fun charge(chargeDTO: CacheService.ChargeDTO): ChargeResponseView {
        try {
            val transaction = this.cacheTransactionRepository.save(this.createCacheTransactionEntity(chargeDTO))
            this.publishAsyncChargingProcessorEvent(eventId = transaction.id!!)

            return ChargeResponseView(
                id = transaction.id!!,
                amount = transaction.amount,
                userId = transaction.userId
            )
        } catch (e: Exception) {
            log.error { e }
            throw CacheChargeException()
        }
    }

    private fun publishAsyncChargingProcessorEvent(eventId: ObjectId) =
        this.eventPublisher.publishEvent(ChargingProduceEventVO(eventId = eventId))

    private fun createCacheTransactionEntity(chargeDTO: CacheService.ChargeDTO) =
        CacheTransaction(
            userId = chargeDTO.userId,
            amount = chargeDTO.amount,
            chargingWayId = chargeDTO.chargingWayId,
            eventType = CacheEventType.CHARGE
        )

    private fun validateToCharge() {

    }

    override fun getBalanceByUserId(userId: Long): CacheBalanceResponseView {
        val cacheBalance = this.cacheBalanceRepository.findById(userId).orElseGet { CacheBalance(userId = userId, balance = 0) }
        return CacheBalanceResponseView(
            userId = cacheBalance.userId,
            balance = cacheBalance.balance
        )
    }

    override fun getChargingWayById(userId: Long): UserChargingWayInfo =
        this.cacheUserChargingWayInfoRepository.findById(userId).orElseThrow { NotExistSavedChargeWayException() }

    override fun getTransactionById(transactionId: ObjectId): CacheTransactionResponseView {
        val transaction = this.cacheTransactionRepository.findById(transactionId).get()
        val userChargingWayInfo = this.cacheUserChargingWayInfoRepository.findById(transaction.chargingWayId).get()
        return CacheTransactionResponseView(
            id = transaction.id.toString(),
            userId = transaction.userId,
            amount = transaction.amount,
            userChargingWayInfo = userChargingWayInfo,
            createdAt = transaction.id!!.date
        )
    }

    override fun updateBalance(userId: Long, amount: Long): CacheBalanceResponseView {
        val balanceOpt = this.cacheBalanceRepository.findById(userId)
        return if (balanceOpt.isPresent) {
            this.cacheBalanceRepository.save(balanceOpt.get().updateBalance(amount = amount))
            CacheBalanceResponseView(
                userId = userId,
                balance = this.cacheBalanceRepository.findById(userId).get().balance
            )
        } else {
            this.cacheBalanceRepository.save(CacheBalance(userId = userId, balance = amount))
            CacheBalanceResponseView(
                userId = userId,
                balance = amount
            )
        }
    }

}