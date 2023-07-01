package com.racket.api.shared.aop

import mu.KotlinLogging
import org.apache.commons.lang3.ObjectUtils
import org.apache.commons.lang3.StringUtils
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component

@Aspect
@Component
class IdParamAspect {

    private val log = KotlinLogging.logger { }

    //@Around("@annotation(com.racket.api.annotation.LongTypeIdInputValidator)")
    @Pointcut("@annotation(com.racket.api.annotation.LongTypeIdInputValidator)")
    fun checkUserIdFormat(pjp: ProceedingJoinPoint): Any {
        log.info("check")
        try {
            var id: String? = null
            require(id == null) { "id 가 NULL 입니다" }
            val parameterValues = pjp.args
            val signature = pjp.signature as MethodSignature
            val method = signature.method
            for (i in method.parameters.indices) {
                val parameterName = method.parameters[i].name
                if (parameterName == "id") {
                    id = parameterValues[i] as String
                    break
                }
            }
            require(ObjectUtils.isNotEmpty(id)) { "id 는 공백일 수 없습니다" }
            require(StringUtils.isNumeric(id)) { "id 는 숫자여야 합니다" }
            return pjp.proceed()

        } catch (e: Exception) {
            throw e
        }
    }
}