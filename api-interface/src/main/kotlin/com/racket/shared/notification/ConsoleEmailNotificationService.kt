package com.racket.shared.notification

import com.racket.shared.notification.domain.EmailNotification
import com.racket.shared.notification.domain.NotificationRepository
import com.racket.shared.notification.infra.EmailNotificationSenderService
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class ConsoleEmailNotificationService(
    val notificationRepository: NotificationRepository
) : EmailNotificationSenderService {

    private val log = KotlinLogging.logger { }

    override fun send(notification: EmailNotification) {
        log.info("email send! >>> $notification")
        this.notificationRepository.save(notification)
    }
}
