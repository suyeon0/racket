package com.racket.api.user

import com.racket.api.shared.annotation.LongTypeIdInputValidator
import com.racket.api.shared.response.ApiResponse
import com.racket.api.user.enums.UserRoleType
import com.racket.api.user.enums.UserStatusType
import com.racket.api.user.request.UserAdditionalInfoCreateRequestCommand
import com.racket.api.user.request.UserCreateRequestCommand
import com.racket.api.user.request.UserUpdateRequestCommand
import com.racket.api.user.response.UserAdditionalResponseView
import com.racket.api.user.response.UserResponseView
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
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

    @LongTypeIdInputValidator
    @PatchMapping("/{id}/additional-info")
    fun putAdditionalInfo(
        @PathVariable id: Long,
        @RequestBody request: UserAdditionalInfoCreateRequestCommand
    ): UserAdditionalResponseView {
        request.validate()
        return this.userService.registerAdditionalUserInformation(
            id = id,
            mobileVO = request.mobileVO,
            addressVO = request.addressVO
        )!!
    }

    @LongTypeIdInputValidator
    @GetMapping("/{id}")
    fun get(@PathVariable id: Long) = this.userService.getUser(id)

    @LongTypeIdInputValidator
    @PatchMapping("/{id}/status")
    fun patchStatus(
        @PathVariable id: Long,
        @RequestParam status: UserStatusType
    ) = this.userService.updateUserStatus(
        id = id,
        status = status
    )


    @LongTypeIdInputValidator
    @PatchMapping("/{id}/role")
    fun patchRole(
        @PathVariable id: Long,
        @RequestParam role: UserRoleType
    ) = this.userService.updateUserRole(
        id = id,
        role = role
    )


    @LongTypeIdInputValidator
    @PatchMapping("/{id}/info")
    fun patchInfo(
        @PathVariable id: Long,
        @RequestBody request: UserUpdateRequestCommand
    ): UserResponseView {
        request.validate()
        return this.userService.updateUserInfo(
            id = id,
            request = request
        )!!
    }

    @LongTypeIdInputValidator
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = this.userService.deleteUser(id)

}