package com.racket.view.auth.advice

import com.racket.api.auth.login.exception.LoginFailException
import com.racket.api.auth.login.exception.NoSuchSessionException
import com.racket.api.shared.response.ApiError
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import javax.servlet.http.HttpServletRequest

@Order(1)
@ControllerAdvice(basePackages = ["com.racket.view.auth"])
class AuthViewExceptionHandlerAdvice {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = [NoSuchSessionException::class])
    fun invalidSessionException(e: LoginFailException, httpServletRequest: HttpServletRequest) =
        ApiError(
            code = HttpStatus.UNAUTHORIZED.value(),
            message = e.message.toString()
        )
}