package com.racket.api.user.advice

import com.racket.api.shared.response.ApiError
import com.racket.api.user.exception.DuplicateUserException
import com.racket.api.user.exception.InvalidUserStatusException
import com.racket.api.user.exception.NotFoundUserException
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest

@Order(1)
@RestControllerAdvice(basePackages = ["com.racket.api.user"])
class UserApiExceptionHandlerAdvice : ResponseEntityExceptionHandler() {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        value = [
            IllegalArgumentException::class, DuplicateUserException::class, NotFoundUserException::class
        ]
    )
    fun userBadRequestException(e: RuntimeException, httpServletRequest: HttpServletRequest) =
        ApiError(
            code = HttpStatus.BAD_REQUEST.value(),
            message = e.message.toString()
        )

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = [InvalidUserStatusException::class])
    fun invalidUserStatusException(e: RuntimeException, httpServletRequest: HttpServletRequest) =
        ApiError(
            code = HttpStatus.UNAUTHORIZED.value(),
            message = e.message.toString()
        )

}