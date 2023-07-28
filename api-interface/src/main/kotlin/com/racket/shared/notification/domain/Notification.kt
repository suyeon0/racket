package com.racket.shared.notification.domain

import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Inheritance
@DiscriminatorColumn(name = "type")
@Table(name = "notifications")
abstract class Notification protected constructor() {

    @Id
    @GeneratedValue
    open var id: Long? = null

    open var receiver: Receiver? = null

    @Column(nullable = false, columnDefinition = "TEXT")
    open var message: String? = null

    @CreationTimestamp
    open var createdAt: LocalDateTime = LocalDateTime.now()

    @Transient
    open var retryCount = 1

    protected constructor(receiver: Receiver, message: String?) : this() {
        this.receiver = receiver
        this.message = message
    }

    open fun increaseRetryCount() {
        this.retryCount++
    }

}



