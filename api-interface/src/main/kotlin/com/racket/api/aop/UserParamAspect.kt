package com.racket.api.aop

import org.apache.commons.lang3.ObjectUtils
import org.apache.commons.lang3.StringUtils
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.reflect.MethodSignature


class UserParamAspect {

    @Around("@annotation(com.racket.api.annotation.UserIdFormat)")
    @Throws(Throwable::class)
    fun checkUserIdFormat(pjp: ProceedingJoinPoint): Any? {
        try {
            var id: String? = null
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
            require(ObjectUtils.isNotEmpty(id)) { "user id 는 공백일 수 없습니다" }
            require(StringUtils.isNumeric(id)) { "user id 는 숫자여야 합니다" }
            return pjp.proceed()

        } catch (e: Exception) {
            throw e
        }
    }
}