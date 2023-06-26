package com.racket.api.user

import com.racket.api.annotation.UserIdInputValidator
import com.racket.api.user.domain.UserRole
import com.racket.api.user.domain.UserStatus
import com.racket.api.user.request.UserAdditionalInfoCreateRequestCommand
import com.racket.api.user.request.UserCreateRequestCommand
import com.racket.api.user.request.UserUpdateRequestCommand
import com.racket.api.user.response.UserAdditionalResponseView
import com.racket.api.user.response.UserResponseView
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Slf4j
@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService) {

    @PostMapping
    fun post(@RequestBody request: UserCreateRequestCommand): ResponseEntity<UserResponseView> {
        request.validate()
        val userRegisterDTO = UserService.UserRegisterDTO(
            userName = request.userName,
            email = request.email,
            password = request.password
        )
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(this.userService.registerUser(userRegisterDTO))
    }

    @UserIdInputValidator
    @PatchMapping("/{id}/additional-info")
    fun putAdditionalInfo(
        @PathVariable id: Long,
        @RequestBody request: UserAdditionalInfoCreateRequestCommand
    ): ResponseEntity<UserAdditionalResponseView> {
        request.validate()
        return ResponseEntity.ok(
            this.userService.registerAdditionalUserInformation(
                id = id,
                mobileVO = request.mobileVO,
                addressVO = request.addressVO
            )
        )
    }

    @UserIdInputValidator
    @GetMapping("/{id}")
    fun get(@PathVariable id: Long) = ResponseEntity.ok(this.userService.getUser(id))

    @UserIdInputValidator
    @PatchMapping("/{id}/status")
    fun patchStatus(
        @PathVariable id: Long,
        @RequestParam status: UserStatus
    ) = ResponseEntity.ok(this.userService.updateUserStatus(
            id = id,
            status = status)
    )

    @UserIdInputValidator
    @PatchMapping("/{id}/role")
    fun patchRole(
        @PathVariable id: Long,
        @RequestParam role: UserRole
    ) = ResponseEntity.ok(this.userService.updateUserRole(
        id = id,
        role = role)
    )

    @UserIdInputValidator
    @PatchMapping("/{id}/info")
    fun patchInfo(
        @PathVariable id: Long,
        @RequestBody request: UserUpdateRequestCommand
    ): ResponseEntity<UserResponseView> {
        request.validate()
        return ResponseEntity.ok(this.userService.updateUserInfo(
            id = id,
            request = request)
        )
    }

    @UserIdInputValidator
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = ResponseEntity.ok(this.userService.deleteUser(id))

}