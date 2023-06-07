package com.racket.api.user.advice

import com.racket.api.user.response.Error
import com.racket.api.user.response.ErrorResponse
import com.racket.view.auth.login.exception.LoginFailException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice(basePackages = ["com.racket.view.auth"])
class GlobalAuthExceptionHandlerAdvice : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [LoginFailException::class])
    fun loginFailException(e: LoginFailException, httpServletRequest: HttpServletRequest) =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse.from(listOf(Error.from(e.message)), httpServletRequest, HttpStatus.BAD_REQUEST.value())
            )

    @ExceptionHandler(value = [Exception::class])
    fun exception(e: Exception, httpServletRequest: HttpServletRequest) =
        ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse.from(
                    listOf(Error.from(e.message.toString())), httpServletRequest, HttpStatus.INTERNAL_SERVER_ERROR.value()
                )
            )

}