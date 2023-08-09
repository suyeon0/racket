package com.racket.cache.core.advice

import com.racket.api.shared.response.ApiError
import com.racket.cache.exception.NotExistSavedChargeWayException
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest

@Order(1)
@RestControllerAdvice(basePackages = ["com.racket.api.cache"])
class CacheApiExceptionHandlerAdvice : ResponseEntityExceptionHandler() {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        value = [
            IllegalArgumentException::class, NotExistSavedChargeWayException::class
        ]
    )
    fun cacheBadRequestException(e: RuntimeException, httpServletRequest: HttpServletRequest) =
        ApiError(
            code = HttpStatus.BAD_REQUEST.value(),
            message = e.message.toString()
        )



}