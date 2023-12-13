package com.racket.api.cash.response

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.bson.types.ObjectId
import util.ObjectIdDeserializer
import util.ObjectIdSerializer

data class ChargeResponseView(

    @JsonSerialize(using = ObjectIdSerializer::class)
    @JsonDeserialize(using = ObjectIdDeserializer::class)
    val id: ObjectId,
    val transactionId: String,
    val userId: Long,
    val amount: Long

)