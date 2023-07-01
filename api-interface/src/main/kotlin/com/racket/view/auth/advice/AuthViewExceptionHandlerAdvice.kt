package com.racket.view.auth.advice

import com.racket.api.auth.login.exception.LoginFailException
import com.racket.api.auth.login.exception.NoSuchSessionException
import com.racket.api.shared.response.Error
import com.racket.api.shared.response.ErrorResponse
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletRequest

@Order(1)
@ControllerAdvice(basePackages = ["com.racket.view.auth"])
class AuthViewExceptionHandlerAdvice {

    @ExceptionHandler(value = [NoSuchSessionException::class])
    fun invalidSessionException(e: LoginFailException, httpServletRequest: HttpServletRequest) =
        ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                ErrorResponse.from(listOf(Error.from(e.message)), httpServletRequest, HttpStatus.UNAUTHORIZED.value())
            )
}