package com.racket.api.user

import com.racket.api.common.vo.AddressVO
import com.racket.api.common.vo.MobileVO
import com.racket.api.user.domain.UserRole
import com.racket.api.user.domain.UserStatus
import com.racket.api.user.request.UserUpdateRequestCommand
import com.racket.api.user.response.UserAdditionalResponseView
import com.racket.api.user.response.UserResponseView

interface UserService {
    fun registerUser(userRegisterDTO: UserRegisterDTO): UserResponseView
    fun updateUserStatus(id: Long, status: UserStatus): UserResponseView
    fun updateUserRole(id: Long, role: UserRole): UserResponseView
    fun getUser(id: Long): UserResponseView?
    fun updateUserInfo(id: Long, request: UserUpdateRequestCommand): UserResponseView?
    fun deleteUser(id: Long): UserResponseView?
    fun registerAdditionalUserInformation(id: Long, mobileVO: MobileVO?, addressVO: AddressVO?): UserAdditionalResponseView?
    fun getUserByEmail(email: String): UserResponseView

    data class UserRegisterDTO(
        val userName: String,
        val email: String,
        val password: String,
    )

}