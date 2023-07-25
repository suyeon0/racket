package com.racket.shared.notification.exception

import com.racket.shared.notification.domain.EmailNotification

class EmailFailException(emailNotification: EmailNotification): RuntimeException() {
    override val message: String = "email send fail: $emailNotification"
}