package com.racket.cash.presentation.response

import org.bson.types.ObjectId

data class ChargeResponseView(

    val id: ObjectId,
    val userId: Long,
    val amount: Long

)