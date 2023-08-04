package com.racket.api.user.events

import com.racket.api.user.vo.UserSignedUpEventVO
import com.racket.shared.notification.domain.EmailNotification
import com.racket.shared.notification.domain.Receiver
import com.racket.shared.notification.infra.EmailNotificationSenderService
import mu.KotlinLogging
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class UserSignedUpEventHandler(
    private val userSignedUpEvent: UserSignedUpEvent
) {

    @Async("userSignedUpSendExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handle(userSignedUpEventVO: UserSignedUpEventVO) {
        val userId = userSignedUpEventVO.userId
        val userName = userSignedUpEventVO.userName

        this.userSignedUpEvent.sendSignedUpSMS(
            userId = userId, userName = userName, userMobileNumber = userSignedUpEventVO.userMobileNumber
        )
        this.userSignedUpEvent.sendSignedUpMail(
            userId = userId, userName = userName, userEmail = userSignedUpEventVO.userEmail
        )
    }
}

@Component
class UserSignedUpEvent(
    private val emailNotificationSenderService: EmailNotificationSenderService
) {

    private val log = KotlinLogging.logger { }

    // 회원 가입 메일 안내
    @Retryable(value = [Exception::class], backoff = Backoff(delay = 3000))
    fun sendSignedUpMail(userId: Long, userEmail: String, userName: String) {
        log.info { "sendSignedUpMail thread Id : ${Thread.currentThread().id}" }
        this.emailNotificationSenderService.send(
            EmailNotification(
                Receiver(userId = userId),
                emailAddress = userEmail,
                title = "회원가입 완료 안내",
                message = "${userName}- 회원가입이 완료되었습니다",
                file = null
            )
        )
    }

    // 회원 가입 SMS 안내
    @Retryable(value = [Exception::class], backoff = Backoff(delay = 3000))
    fun sendSignedUpSMS(userId: Long, userName: String, userMobileNumber: String) {
        log.info {
            "sendSignedUpSMS thread Id : ${Thread.currentThread().id}"
            ">>> sendSignedUpSMS Done"
        }
    }
}