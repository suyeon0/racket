package com.racket.api.cache

import com.racket.api.user.UserService
import org.springframework.stereotype.Service

@Service
class CacheUserServiceImpl(
    private val userService: UserService
): CacheUserService {

    override fun validateUserId(userId: Long) {
        this.userService.getUser(id = userId)
    }


}