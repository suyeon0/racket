package com.racket.api.common.advice

import com.racket.api.common.response.Error
import com.racket.api.common.response.ErrorResponse
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest

@Order
@RestControllerAdvice(basePackages = ["com.racket.api"])
class GlobalApiExceptionHandlerAdvice: ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [RuntimeException::class])
    fun runtimeException(e: Exception, httpServletRequest: HttpServletRequest) =
        ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse.from(
                    listOf(Error.from(e.message.toString())), httpServletRequest, HttpStatus.INTERNAL_SERVER_ERROR.value()
                )
            )
}