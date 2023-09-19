package com.racket.api.cart.advice

import com.racket.api.cart.exception.CartStockException
import com.racket.api.product.exception.NotFoundOptionException
import com.racket.api.product.exception.NotFoundProductException
import com.racket.api.shared.response.ApiError
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest

@Order(1)
@RestControllerAdvice(basePackages = ["com.racket.api.cart"])
class CartApiExceptionHandlerAdvice : ResponseEntityExceptionHandler() {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        value = [IllegalArgumentException::class]
    )
    fun cartBadRequestException(e: RuntimeException, httpServletRequest: HttpServletRequest) =
        ApiError(
            code = HttpStatus.BAD_REQUEST.value(),
            message = e.message.toString()
        )

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(
        value = [CartStockException::class]
    )
    fun cartStockException(e: RuntimeException, httpServletRequest: HttpServletRequest) =
        ApiError(
            code = HttpStatus.BAD_REQUEST.value(),
            message = e.message.toString()
        )
}