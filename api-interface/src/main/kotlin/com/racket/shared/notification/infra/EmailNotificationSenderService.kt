package com.racket.shared.notification.infra

import com.racket.shared.notification.domain.EmailNotification

interface EmailNotificationSenderService: NotificationSender<EmailNotification> {
    override fun send(notification: EmailNotification)
}