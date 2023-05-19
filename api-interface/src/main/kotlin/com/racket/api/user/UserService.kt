package com.racket.api.user

import com.racket.api.common.vo.Address
import com.racket.api.common.vo.Mobile
import com.racket.api.user.domain.UserGrade
import com.racket.api.user.domain.UserStatus
import com.racket.api.user.request.UserUpdateRequestCommand
import com.racket.api.user.response.UserAdditionalResponseView
import com.racket.api.user.response.UserResponseView

interface UserService {
    fun registerUser(userRegisterDTO: UserServiceImpl.UserRegisterDTO): UserResponseView
    fun updateUserStatus(id: Long, status: UserStatus): UserResponseView
    fun updateUserGrade(id: Long, grade: UserGrade): UserResponseView
    fun getUser(id: Long): UserResponseView?
    fun updateUserInfo(id: Long, request: UserUpdateRequestCommand): UserResponseView?
    fun deleteUser(id: Long): UserResponseView?
    fun registerAdditionalUserInformation(id: Long, mobile: Mobile?, address: Address?): UserAdditionalResponseView?

}