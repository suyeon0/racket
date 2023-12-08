package com.racket.api.shared.advice

import com.racket.api.shared.annotation.SlackNotification
import com.racket.api.shared.response.ApiError
import com.racket.shared.notification.exception.EmailFailException
import mu.KotlinLogging
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest

@Order
@RestControllerAdvice(basePackages = ["com.racket.api"])
class GlobalApiExceptionHandlerAdvice : ResponseEntityExceptionHandler() {

    private val log = KotlinLogging.logger { }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = [EmailFailException::class])
    fun emailException(e: EmailFailException, httpServletRequest: HttpServletRequest): ApiError {
        log.error { e.stackTrace }
        return ApiError(
            code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = e.message
    )
}

    @SlackNotification
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = [RuntimeException::class])
    fun runtimeException(e: RuntimeException, httpServletRequest: HttpServletRequest) =
        ApiError(
            code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = e.message.toString()
        )
}