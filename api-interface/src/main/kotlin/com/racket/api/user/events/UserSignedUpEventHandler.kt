package com.racket.api.user.events

import com.racket.api.user.vo.UserSignedUpEventVO
import com.racket.shared.notification.domain.EmailNotification
import com.racket.shared.notification.domain.Receiver
import com.racket.shared.notification.infra.EmailNotificationSenderService
import mu.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

@Component
class UserSignedUpEventHandler(
    private val emailNotificationSenderService: EmailNotificationSenderService,
    private val eventPublisher: ApplicationEventPublisher
) {

    private val log = KotlinLogging.logger { }

    @Async("userSignedUpMailSendExecutor")
    @TransactionalEventListener(
        classes = [UserSignedUpEventVO::class],
        phase = TransactionPhase.AFTER_COMMIT
    )
    fun handle(userSignedUpEventVO: UserSignedUpEventVO) {
        log.info {
            "UserSignedUpEventHandler Thread ID : ${Thread.currentThread().id}"
        }
        val retryCount = userSignedUpEventVO.retryCount
        val userEmail = userSignedUpEventVO.userEmail
        val userName = userSignedUpEventVO.userName
        val userId = userSignedUpEventVO.userId

        // 회원 가입 이메일 전송
        if (this.sendEmail(retryCount = retryCount, userEmail = userEmail, userName = userName, userId = userId)) {
            // 포인트 충전
            this.chargePoint(userId = userId)
        }

        this.setCache(userId)
    }

    private fun setCache(userId: Long) {
        // 캐시 설정
        // 캐시로 회원 정보를 저장한다.
    }

    private fun sendEmail(retryCount: Int, userEmail: String, userName: String, userId: Long) =
        try {
            this.emailNotificationSenderService.send(
                EmailNotification(
                    Receiver(userId = userId),
                    emailAddress = userEmail,
                    title = "회원가입 완료 안내",
                    message = "${userName}- 회원가입이 완료되었습니다",
                    file = null
                )
            )
            true
        } catch (e: Exception) {
            TimeUnit.SECONDS.sleep(3)
            if (retryCount < 3) {
                this.eventPublisher.publishEvent(
                    UserSignedUpEventVO(retryCount = retryCount+1, userName = userName, userEmail = userEmail, userId = userId)
                )
                false
            } else {
                this.log.error {
                    "이메일 발송 재시도 실패: userId: ${userId}, email: ${userEmail}, userName: ${userName}"
                }
                true
            }
        }

    private fun chargePoint(userId: Long) {
        // 포인트 충전
    }
}