package com.racket.api.delivery.common.advice

import com.racket.api.shared.response.ApiError
import com.racket.api.delivery.common.exception.TrackingClientFailException
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletRequest

@Order(1)
@RestControllerAdvice(basePackages = ["com.racket.api.delivery"])
class DeliveryApiExceptionHandlerAdvice {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(
        value = [
            TrackingClientFailException::class
        ]
    )
    fun deliveryInternalServerErrorException(e: RuntimeException, httpServletRequest: HttpServletRequest) =
        ApiError(
            code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = e.message.toString()
        )

}