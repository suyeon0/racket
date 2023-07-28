package com.racket.api.auth.login.advice

import com.racket.api.auth.login.exception.LoginFailException
import com.racket.api.auth.login.exception.NoSuchSessionException
import com.racket.api.shared.response.ApiError
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest

@Order(1)
@RestControllerAdvice(basePackages = ["com.racket.api.auth"])
class AuthApiExceptionHandlerAdvice : ResponseEntityExceptionHandler() {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [LoginFailException::class])
    fun loginFailException(e: LoginFailException, httpServletRequest: HttpServletRequest) =
        ApiError(
            code = HttpStatus.BAD_REQUEST.value(),
            message = e.message.toString()
        )

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = [NoSuchSessionException::class])
    fun invalidSessionException(e: LoginFailException, httpServletRequest: HttpServletRequest) =
        ApiError(
            code = HttpStatus.UNAUTHORIZED.value(),
            message = e.message.toString()
        )

}