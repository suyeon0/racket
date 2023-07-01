package com.racket.api.product.advice

import com.racket.api.shared.response.Error
import com.racket.api.shared.response.ErrorResponse
import com.racket.api.product.exception.NotFoundOptionException
import com.racket.api.product.exception.NotFoundProductException
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest

@Order(1)
@RestControllerAdvice(basePackages = ["com.racket.api.product"])
class ProductApiExceptionHandlerAdvice: ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [
        IllegalArgumentException::class
        , NotFoundProductException::class
        , NotFoundOptionException::class
    ])
    fun productBadRequestException(e: RuntimeException, httpServletRequest: HttpServletRequest) =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse.from(listOf(Error.from(e.message.toString())), httpServletRequest, HttpStatus.BAD_REQUEST.value())
            )


}