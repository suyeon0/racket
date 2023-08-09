package com.racket.cache.presentation.response

import com.racket.cache.entity.UserChargingWayInfo
import java.util.*

data class CacheTransactionResponseView(

    val id: String,
    val userId: Long,
    val amount: Long,
    val userChargingWayInfo: UserChargingWayInfo,
    val createdAt: Date

)