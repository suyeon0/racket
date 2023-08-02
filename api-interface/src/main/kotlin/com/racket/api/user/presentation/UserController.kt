package com.racket.api.user.presentation

import com.racket.api.shared.annotation.LongTypeIdInputValidator
import com.racket.api.shared.annotation.SwaggerFailResponse
import com.racket.api.user.UserService
import com.racket.api.user.domain.enums.UserRoleType
import com.racket.api.user.domain.enums.UserStatusType
import com.racket.api.user.presentation.request.UserAdditionalInfoCreateRequestCommand
import com.racket.api.user.presentation.request.UserCreateRequestCommand
import com.racket.api.user.presentation.request.UserUpdateRequestCommand
import com.racket.api.user.presentation.response.UserAdditionalResponseView
import com.racket.api.user.presentation.response.UserResponseView
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "유저 API")
@RestController
@RequestMapping("/api/v1/user")
class UserController(private val userService: UserService) {

    @SwaggerFailResponse
    @PostMapping
    @Operation(
        summary = "유저 등록",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Success",
                content = [Content(schema = Schema(implementation = UserResponseView::class))]
            ),
            ApiResponse(responseCode = "400", description = "Bad Request"),
            ApiResponse(responseCode = "404", description = "Not Found"),
            ApiResponse(responseCode = "500", description = "Internal Server Error")
        ]
    )
    fun post(@RequestBody request: UserCreateRequestCommand): ResponseEntity<UserResponseView> {
        request.validate()
        val userRegisterDTO = UserService.UserRegisterDTO(
            userName = request.userName,
            email = request.email,
            password = request.password,
            mobileVO = request.mobileVO
        )
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(this.userService.registerUser(userRegisterDTO))
    }

    @SwaggerFailResponse
    @LongTypeIdInputValidator
    @Operation(
        summary = "유저 추가 정보 등록",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = UserAdditionalResponseView::class))]
            ),
            ApiResponse(responseCode = "400", description = "Bad Request"),
            ApiResponse(responseCode = "404", description = "Not Found"),
            ApiResponse(responseCode = "500", description = "Internal Server Error")
        ]
    )
    @PatchMapping("/{id}/additional-info")
    fun putAdditionalInfo(
        @PathVariable id: Long,
        @RequestBody request: UserAdditionalInfoCreateRequestCommand
    ): ResponseEntity<UserAdditionalResponseView> {
        request.validate()
        return ResponseEntity.ok(
            this.userService.registerAdditionalUserInformation(
                id = id,
                addressVO = request.addressVO
            )
        )
    }

    @SwaggerFailResponse
    @GetMapping("/{id}")
    @LongTypeIdInputValidator
    @Operation(
        summary = "유저 조회",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = UserResponseView::class))]
            ),
            ApiResponse(responseCode = "400", description = "Bad Request"),
            ApiResponse(responseCode = "404", description = "Not Found User"),
            ApiResponse(responseCode = "500", description = "Internal Server Error")
        ]
    )
    fun get(@PathVariable id: Long) = ResponseEntity.ok(this.userService.getUser(id))

    @SwaggerFailResponse
    @LongTypeIdInputValidator
    @PatchMapping("/{id}/status")
    @Operation(
        summary = "유저 상태 변경",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = UserResponseView::class))]
            ),
            ApiResponse(responseCode = "400", description = "Bad Request"),
            ApiResponse(responseCode = "404", description = "Not Found User"),
            ApiResponse(responseCode = "500", description = "Internal Server Error")
        ]
    )
    fun patchStatus(@PathVariable id: Long, @RequestParam status: UserStatusType) = ResponseEntity.ok(
        this.userService.updateUserStatus(
            id = id,
            status = status
        )
    )

    @SwaggerFailResponse
    @LongTypeIdInputValidator
    @PatchMapping("/{id}/role")
    @Operation(
        summary = "유저 롤 변경",
        parameters = [Parameter(name = "id", description = "유저 ID", example = "1")],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = UserResponseView::class))]
            ),
            ApiResponse(responseCode = "400", description = "Bad Request"),
            ApiResponse(responseCode = "404", description = "Not Found User"),
            ApiResponse(responseCode = "500", description = "Internal Server Error")
        ]
    )
    fun patchRole(@PathVariable id: Long, @RequestParam role: UserRoleType) = ResponseEntity.ok(
        this.userService.updateUserRole(
            id = id,
            role = role
        )
    )

    @SwaggerFailResponse
    @LongTypeIdInputValidator
    @PatchMapping("/{id}/info")
    @Operation(
        summary = "유저 정보 변경",
        parameters = [Parameter(name = "id", description = "유저 ID", example = "1")],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = UserResponseView::class))]
            ),
            ApiResponse(responseCode = "400", description = "Bad Request"),
            ApiResponse(responseCode = "404", description = "Not Found User"),
            ApiResponse(responseCode = "500", description = "Internal Server Error")
        ]
    )
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
    @SwaggerFailResponse
    @LongTypeIdInputValidator
    @Operation(
        summary = "유저 정보 삭제",
        parameters = [Parameter(name = "id", description = "유저 ID", example = "1")],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = UserResponseView::class))]
            ),
            ApiResponse(responseCode = "400", description = "Bad Request"),
            ApiResponse(responseCode = "404", description = "Not Found User"),
            ApiResponse(responseCode = "500", description = "Internal Server Error")
        ]
    )
    fun delete(@PathVariable id: Long) = ResponseEntity.ok(this.userService.deleteUser(id))

}