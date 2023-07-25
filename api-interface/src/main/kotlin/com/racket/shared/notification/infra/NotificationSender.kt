package com.racket.shared.notification.infra

import com.racket.shared.notification.domain.Notification

interface NotificationSender<T : Notification> {
    fun send(notification: T)
}