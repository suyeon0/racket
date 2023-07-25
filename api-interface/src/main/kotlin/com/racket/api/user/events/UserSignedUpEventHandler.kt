package com.racket.api.user.events

import com.racket.api.user.vo.UserSignedUpEventVO
import com.racket.shared.notification.domain.EmailNotification
import com.racket.shared.notification.domain.Receiver
import com.racket.shared.notification.infra.EmailNotificationSenderService
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class UserSignedUpEventHandler(
    private val emailNotificationSenderService: EmailNotificationSenderService
) {

    private val log = KotlinLogging.logger { }

    @Async("userSignedUpMailSendExecutor")
    @TransactionalEventListener(
        classes = [UserSignedUpEventVO::class],
        phase = TransactionPhase.AFTER_COMMIT
    )
    fun handle(userSignedUpEventVO: UserSignedUpEventVO) {
        log.info("UserSignedUpEventHandler Thread ID : " + Thread.currentThread().id)

        val emailNotification = EmailNotification(
            Receiver(userId = userSignedUpEventVO.userId),
            emailAddress = userSignedUpEventVO.userEmail,
            title = "회원가입 완료 안내",
            message = userSignedUpEventVO.userName + "- 회원가입이 완료되었습니다",
            file = null
        )
        this.emailNotificationSenderService.send(emailNotification)
    }
}