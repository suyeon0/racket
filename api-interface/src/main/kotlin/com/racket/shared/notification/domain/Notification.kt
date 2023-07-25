package com.racket.shared.notification.domain

import lombok.Getter
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@DiscriminatorColumn(name = "type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
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

    protected constructor(receiver: Receiver, message: String?) : this() {
        this.receiver = receiver
        this.message = message
    }

}



