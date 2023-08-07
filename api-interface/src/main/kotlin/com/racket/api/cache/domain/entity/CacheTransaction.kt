package com.racket.api.cache.domain.entity

import com.racket.api.cache.domain.enums.CacheEventType
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import javax.persistence.*

@Document("cacheTransaction")
class CacheTransaction(

    @Id
    @Column(name = "cache_transaction_id", nullable = false)
    var id: ObjectId? = null,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "amount", nullable = false)
    val amount: Long,

    @Column(name = "event_type", nullable = false)
    val eventType: CacheEventType,

    @Column(name = "charging_way", nullable = false)
    val chargingWayId: Long

)