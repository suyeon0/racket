package com.racket.api.shared.user

import com.racket.share.domain.user.UserRepository
import com.racket.share.domain.user.exception.InvalidUserStatusException
import com.racket.share.domain.user.exception.NotFoundUserException
import org.springframework.stereotype.Component

@Component
class BaseUserComponent(
    private val userRepository: UserRepository
) {

    fun validateUserByUserId(userId: Long) {
        val user = this.userRepository.findById(userId)
            .orElseThrow { NotFoundUserException() }

        if (user.isDeletedStatus()) {
            throw InvalidUserStatusException()
        }
    }

}