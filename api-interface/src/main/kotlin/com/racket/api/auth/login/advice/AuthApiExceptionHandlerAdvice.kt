package com.racket.api.auth.login.advice

import com.racket.api.shared.response.Error
import com.racket.api.shared.response.ErrorResponse
import com.racket.api.auth.login.exception.LoginFailException
import com.racket.api.auth.login.exception.NoSuchSessionException
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest

@Order(1)
@RestControllerAdvice(basePackages = ["com.racket.api.auth"])
class AuthApiExceptionHandlerAdvice : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [LoginFailException::class])
    fun loginFailException(e: LoginFailException, httpServletRequest: HttpServletRequest) =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse.from(listOf(Error.from(e.message)), httpServletRequest, HttpStatus.BAD_REQUEST.value())
            )

    @ExceptionHandler(value = [NoSuchSessionException::class])
    fun invalidSessionException(e: LoginFailException, httpServletRequest: HttpServletRequest) =
        ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                ErrorResponse.from(listOf(Error.from(e.message)), httpServletRequest, HttpStatus.UNAUTHORIZED.value())
            )

}