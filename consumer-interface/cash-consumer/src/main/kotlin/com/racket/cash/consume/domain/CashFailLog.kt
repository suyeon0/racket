package com.racket.cash.consume.domain

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.sql.Timestamp
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.PrePersist




@Document("cash_fail_log")
class CashFailLog(

    @Id
    @Column(name = "id", nullable = false)
    private var id: ObjectId? = null,

    @Column(name = "payload")
    private val payload: String,

    @Column(name = "error_message", columnDefinition = "TEXT")
    private val errorMessage: String,

    @Column(name = "timestamp", nullable = false)
    private var timestamp: Timestamp? = null

) {
    @PrePersist
    fun prePersist() {
        timestamp = Timestamp.from(Instant.now())
    }
}