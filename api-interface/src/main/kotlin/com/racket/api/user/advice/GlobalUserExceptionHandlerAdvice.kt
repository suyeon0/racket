package com.racket.api.user.advice

import com.racket.api.user.exception.DuplicateUserException
import com.racket.api.user.exception.InvalidUserStatusException
import com.racket.api.user.exception.NotFoundUserException
import com.racket.api.user.response.Error
import com.racket.api.user.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice(basePackages = ["com.racket.api.user"])
class GlobalUserExceptionHandlerAdvice : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [IllegalArgumentException::class])
    fun illegalArgumentException(e: IllegalArgumentException, httpServletRequest: HttpServletRequest) =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse.from(listOf(Error.from(e.message.toString())), httpServletRequest, HttpStatus.BAD_REQUEST.value())
            )

    @ExceptionHandler(value = [DuplicateUserException::class])
    fun duplicateUserException(e: DuplicateUserException, httpServletRequest: HttpServletRequest) =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse.from(listOf(Error.from(e.message)), httpServletRequest, HttpStatus.BAD_REQUEST.value())
            )

    @ExceptionHandler(value = [NotFoundUserException::class])
    fun notFountUserException(e: NotFoundUserException, httpServletRequest: HttpServletRequest) =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse.from(listOf(Error.from(e.message)), httpServletRequest, HttpStatus.BAD_REQUEST.value())
            )

    @ExceptionHandler(value = [InvalidUserStatusException::class])
    fun invalidUserStatusException(e: InvalidUserStatusException, httpServletRequest: HttpServletRequest) =
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