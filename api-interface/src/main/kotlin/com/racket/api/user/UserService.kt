package com.racket.api.user

import com.racket.api.shared.vo.AddressVO
import com.racket.api.shared.vo.MobileVO
import com.racket.api.user.domain.User
import com.racket.api.user.enums.UserRoleType
import com.racket.api.user.enums.UserStatusType
import com.racket.api.user.request.UserUpdateRequestCommand
import com.racket.api.user.response.UserAdditionalResponseView
import com.racket.api.user.response.UserResponseView
import java.util.*

interface UserService {
    fun registerUser(userRegisterDTO: UserRegisterDTO): UserResponseView
    fun updateUserStatus(id: Long, status: UserStatusType): UserResponseView
    fun updateUserRole(id: Long, role: UserRoleType): UserResponseView
    fun getUser(id: Long): UserResponseView?
    fun updateUserInfo(id: Long, request: UserUpdateRequestCommand): UserResponseView?
    fun deleteUser(id: Long): UserResponseView?
    fun registerAdditionalUserInformation(id: Long, addressVO: AddressVO?): UserAdditionalResponseView?
    fun getUserByEmail(email: String): UserResponseView

    fun getUserByEmailAndPassword(email: String, password: String): Optional<User>

    data class UserRegisterDTO(
        val userName: String,
        val email: String,
        val password: String,
        val mobileVO: MobileVO
    )

}