package com.racket.api.user.advice

import com.racket.api.user.exception.DuplicateUserException
import com.racket.api.user.exception.InvalidUserStatusException
import com.racket.api.user.exception.NotFoundUserException
import com.racket.api.common.response.Error
import com.racket.api.common.response.ErrorResponse
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest

@Order(1)
@RestControllerAdvice(basePackages = ["com.racket.api.user"])
class UserApiExceptionHandlerAdvice : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [
          IllegalArgumentException::class
        , DuplicateUserException::class
        , NotFoundUserException::class
    ])
    fun userBadRequestException(e: RuntimeException, httpServletRequest: HttpServletRequest) =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse.from(listOf(Error.from(e.message.toString())), httpServletRequest, HttpStatus.BAD_REQUEST.value())
            )

    @ExceptionHandler(value = [InvalidUserStatusException::class])
    fun invalidUserStatusException(e: RuntimeException, httpServletRequest: HttpServletRequest) =
        ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                ErrorResponse.from(listOf(Error.from(e.message.toString())), httpServletRequest, HttpStatus.UNAUTHORIZED.value())
            )

}