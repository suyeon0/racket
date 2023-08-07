package com.racket.api.cache

import com.racket.api.cache.domain.CacheBalanceRepository
import com.racket.api.cache.domain.CacheTransactionRepository
import com.racket.api.cache.domain.UserChargingWayInfoRepository
import com.racket.api.cache.domain.entity.CacheBalance
import com.racket.api.cache.domain.entity.CacheTransaction
import com.racket.api.cache.domain.entity.UserChargingWayInfo
import com.racket.api.cache.domain.enums.CacheEventType
import com.racket.api.cache.exception.NotExistSavedChargeWay
import com.racket.api.cache.presentation.reponse.CacheBalanceResponseView
import com.racket.api.cache.presentation.reponse.ChargeResponseView
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CacheServiceImpl(
    val cacheTransactionRepository: CacheTransactionRepository,
    val cacheBalanceRepository: CacheBalanceRepository,
    val cacheUserChargingWayInfoRepository: UserChargingWayInfoRepository,
    val mongoTemplate: MongoTemplate

): CacheService {
    override fun charge(chargeDTO: CacheService.ChargeDTO): ChargeResponseView {
        val transaction = this.cacheTransactionRepository.save(this.createCacheTransactionEntity(chargeDTO))
        return ChargeResponseView(
            id = transaction.id!!,
            amount = transaction.amount,
            userId = transaction.userId
        )
    }

    private fun createCacheTransactionEntity(chargeDTO: CacheService.ChargeDTO) =
        CacheTransaction (
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

    override fun getChargingWayById(id: Long): UserChargingWayInfo =
        this.cacheUserChargingWayInfoRepository.findById(id).orElseThrow { NotExistSavedChargeWay() }

}