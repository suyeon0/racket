package com.racket.cash.core.advice

import com.racket.api.shared.response.ApiError
import com.racket.cash.exception.InvalidChargingTransactionException
import com.racket.cash.exception.NotExistSavedChargeWayException
import com.racket.cash.exception.NotFoundCashTransactionException
import com.racket.cash.exception.ChargingProcessingException
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest

@Order(1)
@RestControllerAdvice(basePackages = ["com.racket.cash"])
class CashApiExceptionHandlerAdvice : ResponseEntityExceptionHandler() {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        value = [
            IllegalArgumentException::class,
            NotExistSavedChargeWayException::class,
            InvalidChargingTransactionException::class,
            NotFoundCashTransactionException::class
        ]
    )
    fun cashBadRequestException(e: RuntimeException, httpServletRequest: HttpServletRequest) =
        ApiError(
            code = HttpStatus.BAD_REQUEST.value(),
            message = e.message.toString()
        )


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(
        value = [
            ChargingProcessingException::class
        ]
    )
    fun cashInternalServerError(e: RuntimeException, httpServletRequest: HttpServletRequest) =
        ApiError(
            code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = e.message.toString()
        )

}