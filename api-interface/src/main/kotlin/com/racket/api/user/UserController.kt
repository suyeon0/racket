package com.racket.api.user

import com.racket.api.annotation.UserIdFormat
import com.racket.api.user.domain.UserGrade
import com.racket.api.user.domain.UserStatus
import com.racket.api.user.request.UserCreateRequestCommand
import com.racket.api.user.request.UserUpdateRequestCommand
import com.racket.api.user.response.UserResponseView
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.Validator

@Slf4j
@RestController
@RequestMapping("/user")
class UserController(private val userService: UserService, val validator: Validator) {

    @PostMapping
    fun post(@Valid @RequestBody request: UserCreateRequestCommand): ResponseEntity<UserResponseView> {
        val userCreateResponse: UserResponseView = this.userService.registerUser(request)
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(
                userCreateResponse
            )
    }

    @UserIdFormat
    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<UserResponseView> {
        val userResponse = this.userService.getUser(id)
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                userResponse
            )
    }

    @UserIdFormat
    @PatchMapping("/{id}/status")
    fun patchStatus(
        @PathVariable id: Long,
        @RequestParam status: UserStatus
    ): ResponseEntity<UserResponseView> {
        val patchUserStatusResponse: UserResponseView = this.userService.updateUserStatus(id, status)
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                patchUserStatusResponse
            )
    }

    @UserIdFormat
    @PatchMapping("/{id}/grade")
    fun patchGrade(
        @PathVariable id: Long,
        @RequestParam grade: UserGrade
    ): ResponseEntity<UserResponseView> {
        val patchUserGradeResponse: UserResponseView = this.userService.updateUserGrade(id, grade)
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                patchUserGradeResponse
            )
    }

    @UserIdFormat
    @PutMapping("/{id}/info")
    fun patchInfo(
        @PathVariable id: Long,
        @Valid @RequestBody request: UserUpdateRequestCommand
    ): ResponseEntity<UserResponseView> {
        val patchUserInfoResponse: UserResponseView? = this.userService.updateUserInfo(id, request)
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                patchUserInfoResponse
            )
    }

    @UserIdFormat
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<UserResponseView> {
        val deleteUserInfoResponse: UserResponseView? = this.userService.deleteUser(id)
        return ResponseEntity.status(HttpStatus.OK)
            .body(deleteUserInfoResponse)
    }

}