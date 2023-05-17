package com.racket.api.user

import com.racket.api.user.domain.UserGrade
import com.racket.api.user.domain.UserStatus
import com.racket.api.user.request.UserCreateRequestCommand
import com.racket.api.user.request.UserUpdateRequestCommand
import com.racket.api.user.response.UserResponseView

interface UserService {
    abstract fun registerUser(request: UserCreateRequestCommand): UserResponseView
    abstract fun updateUserStatus(id: Long, status: UserStatus): UserResponseView
    abstract fun updateUserGrade(id: Long, grade: UserGrade): UserResponseView
    abstract fun getUser(id: Long): UserResponseView?
    abstract fun updateUserInfo(id: Long, request: UserUpdateRequestCommand): UserResponseView?
    abstract fun deleteUser(id: Long): UserResponseView?

}