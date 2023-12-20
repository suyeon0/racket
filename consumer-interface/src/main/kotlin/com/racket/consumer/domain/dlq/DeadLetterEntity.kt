package com.racket.consumer.domain.dlq

import org.bson.types.ObjectId
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class DeadLetterEntity (
    @Id
    @Column(name = "id", nullable = false)
    var id: ObjectId? = null,

    @Column(name = "failure_topic", nullable = false)
    val failureTopic: String,

    @Column(name = "key", nullable = false)
    val key: String,

    @Column(name = "payload", nullable = false)
    val payload: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "is_processed", nullable = false)
    val isProcessed: Boolean
)