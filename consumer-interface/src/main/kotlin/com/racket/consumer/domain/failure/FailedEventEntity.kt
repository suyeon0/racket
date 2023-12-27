package com.racket.consumer.domain.failure

import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "failed_event")
data class FailedEventEntity (
    @Id
    @Column(name = "id", nullable = false)
    var id: String? = null,

    @Column(name = "origin_event_timestamp", nullable = false)
    var originEventTimestamp: Instant,

    @Column(name = "topic", nullable = false)
    val topic: String,

    @Column(name = "message_key", nullable = false)
    val key: String,

    @Column(name = "payload", nullable = false)
    val payload: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant,

    @Column(name = "processed", nullable = false)
    val isProcessed: Boolean
)