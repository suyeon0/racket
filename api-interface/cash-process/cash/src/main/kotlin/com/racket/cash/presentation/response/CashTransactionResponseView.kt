package com.racket.cash.presentation.response

import com.racket.cash.entity.UserChargingWayInfo
import java.util.*

data class CashTransactionResponseView(

    val id: String,
    val userId: Long,
    val amount: Long,
    val userChargingWayInfo: UserChargingWayInfo,
    val createdAt: Date

)