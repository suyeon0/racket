package com.racket.api.user.common

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
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [DuplicateUserException::class])
    fun duplicateUserException(e: DuplicateUserException, httpServletRequest: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val statusCode: Int = HttpStatus.BAD_REQUEST.value()

        val errorList: MutableList<Error> = mutableListOf()
        errorList.add(Error.from(e.message))

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.from(errorList, httpServletRequest, statusCode))
    }

    @ExceptionHandler(value = [NotFoundUserException::class])
    fun notFountUserException(e: NotFoundUserException, httpServletRequest: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val statusCode: Int = HttpStatus.BAD_REQUEST.value()

        val errorList: MutableList<Error> = mutableListOf()
        errorList.add(Error.from(e.message))

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.from(errorList, httpServletRequest, statusCode))
    }

    @ExceptionHandler(value = [InvalidUserStatusException::class])
    fun invalidUserStatusException(e: InvalidUserStatusException, httpServletRequest: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val statusCode: Int = HttpStatus.BAD_REQUEST.value()

        val errorList: MutableList<Error> = mutableListOf()
        errorList.add(Error.from(e.message))

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.from(errorList, httpServletRequest, statusCode))
    }

}