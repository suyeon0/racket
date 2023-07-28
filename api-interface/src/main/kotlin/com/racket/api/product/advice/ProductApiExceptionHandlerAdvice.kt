package com.racket.api.product.advice

import com.racket.api.shared.response.ApiError
import com.racket.api.product.exception.NotFoundOptionException
import com.racket.api.product.exception.NotFoundProductException
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest

@Order(1)
@RestControllerAdvice(basePackages = ["com.racket.api.product"])
class ProductApiExceptionHandlerAdvice : ResponseEntityExceptionHandler() {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        value = [IllegalArgumentException::class]
    )
    fun productBadRequestException(e: RuntimeException, httpServletRequest: HttpServletRequest) =
        ApiError(
            code = HttpStatus.BAD_REQUEST.value(),
            message = e.message.toString()
        )

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(
        value = [NotFoundProductException::class, NotFoundOptionException::class]
    )
    fun productNotFoundException(e: RuntimeException, httpServletRequest: HttpServletRequest) =
        ApiError(
            code = HttpStatus.BAD_REQUEST.value(),
            message = e.message.toString()
        )
}