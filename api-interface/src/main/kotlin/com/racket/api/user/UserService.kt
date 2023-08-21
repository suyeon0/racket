package com.racket.api.user

import com.racket.api.user.presentation.request.UserUpdateRequestCommand
import com.racket.api.user.presentation.response.UserAdditionalResponseView
import com.racket.api.user.presentation.response.UserResponseView
import com.racket.share.domain.user.User
import com.racket.share.domain.user.enums.UserRoleType
import com.racket.share.domain.user.enums.UserStatusType
import com.racket.share.vo.AddressVO
import com.racket.share.vo.MobileVO
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