package com.racket.api.shared.aop

import com.racket.shared.notification.exception.slack.RequestStorage
import com.racket.shared.notification.exception.slack.SlackMessageGenerator
import mu.KotlinLogging
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Aspect
@Component
class ExceptionAppenderAspect(
    private val requestStorage: RequestStorage,
    private val slackMessageGenerator: SlackMessageGenerator
) {

    /**
     * bean 으로 등록하니까 static 일 필요 없음
     */
    private val log = KotlinLogging.logger { }

    @Before("@annotation(com.racket.api.shared.annotation.SlackNotification)")
    fun appendResponseBodyToSlackContent(joinPoint: JoinPoint) {
        val args = joinPoint.args

        val message = this.slackMessageGenerator.generate(request = requestStorage.get(), exception = args[0] as Exception)
        log.info { message }
    }
}