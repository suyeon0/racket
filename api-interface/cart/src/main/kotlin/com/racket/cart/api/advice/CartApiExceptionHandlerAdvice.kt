package com.racket.cart.api.advice

import com.racket.cart.api.exception.CartStockException
import com.racket.cart.api.exception.NotFoundCartItemException
import com.racket.api.shared.response.ApiError
import com.racket.cart.api.exception.CartDeliveryFeignException
import com.racket.cart.api.exception.CartProductFeignException
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest

@Order(1)
@RestControllerAdvice(basePackages = ["com.racket.cart.api"])
class CartApiExceptionHandlerAdvice : ResponseEntityExceptionHandler() {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        value = [
            IllegalArgumentException::class
        ]
    )
    fun cartBadRequestException(e: RuntimeException, httpServletRequest: HttpServletRequest) =
        ApiError(
            code = HttpStatus.BAD_REQUEST.value(),
            message = e.message.toString()
        )

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(
        value = [
            NotFoundCartItemException::class
        ]
    )
    fun cartNotFoundException(e: RuntimeException, httpServletRequest: HttpServletRequest) =
        ApiError(
            code = HttpStatus.NOT_FOUND.value(),
            message = e.message.toString()
        )

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(
        value = [
            CartStockException::class
        ]
    )
    fun cartStockException(e: RuntimeException, httpServletRequest: HttpServletRequest) =
        ApiError(
            code = HttpStatus.BAD_REQUEST.value(),
            message = e.message.toString()
        )

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(
        value = [
            CartDeliveryFeignException::class,
            CartProductFeignException::class
        ]
    )
    fun cartFeignException(e: RuntimeException, httpServletRequest: HttpServletRequest) =
        ApiError(
            code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = e.message.toString()
        )

}