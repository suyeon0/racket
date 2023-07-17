package com.racket.api.user

import com.racket.api.shared.annotation.LongTypeIdInputValidator
import com.racket.api.user.enums.UserRoleType
import com.racket.api.user.enums.UserStatusType
import com.racket.api.user.request.UserAdditionalInfoCreateRequestCommand
import com.racket.api.user.request.UserCreateRequestCommand
import com.racket.api.user.request.UserUpdateRequestCommand
import com.racket.api.user.response.UserAdditionalResponseView
import com.racket.api.user.response.UserResponseView
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "유저 API")
@RestController
@RequestMapping("/api/v1/user")
class UserController(private val userService: UserService) {

    @PostMapping
    @Operation(summary = "유저 등록")
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
    @Operation(summary = "유저 추가 정보 등록")
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

    @GetMapping("/{id}")
    @LongTypeIdInputValidator
    @Operation(summary = "유저 조회")
    fun get(@PathVariable id: Long) = ResponseEntity.ok(this.userService.getUser(id))

    @LongTypeIdInputValidator
    @PatchMapping("/{id}/status")
    @Operation(summary = "유저 상태 변경")
    fun patchStatus(@PathVariable id: Long, @RequestParam status: UserStatusType) = ResponseEntity.ok(
        this.userService.updateUserStatus(
            id = id,
            status = status
        )
    )

    @LongTypeIdInputValidator
    @PatchMapping("/{id}/role")
    @Operation(summary = "유저 롤 변경")
    fun patchRole(@PathVariable id: Long, @RequestParam role: UserRoleType) = ResponseEntity.ok(
        this.userService.updateUserRole(
            id = id,
            role = role
        )
    )

    @LongTypeIdInputValidator
    @PatchMapping("/{id}/info")
    @Operation(summary = "유저 정보 변경")
    fun patchInfo(
        @PathVariable id: Long,
        @RequestBody request: UserUpdateRequestCommand
    ): ResponseEntity<UserResponseView> {
        request.validate()
        return ResponseEntity.ok(
            this.userService.updateUserInfo(
                id = id,
                request = request
            )!!
        )
    }

    @DeleteMapping("/{id}")
    @LongTypeIdInputValidator
    @Operation(summary = "유저 삭제")
    fun delete(@PathVariable id: Long) = ResponseEntity.ok(this.userService.deleteUser(id))

}