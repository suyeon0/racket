package com.racket.api.user

import com.racket.api.user.domain.User
import com.racket.api.user.domain.UserGrade
import com.racket.api.user.domain.UserStatus
import com.racket.api.user.exception.DuplicateUserException
import com.racket.api.user.exception.InvalidUserStatusException
import com.racket.api.user.exception.NotFoundUserException
import com.racket.api.user.request.UserCreateRequestCommand
import com.racket.api.user.request.UserUpdateRequestCommand
import com.racket.api.user.response.UserResponseView
import org.springframework.stereotype.Service


@Service
internal class UserServiceImpl(private val userRepository: UserRepository)
    : UserService {

    /**
     * 회원 등록
     */
    override fun registerUser(request: UserCreateRequestCommand): UserResponseView {
        this.checkDuplicateEmail(request.email)

        val user = this.userRepository.save(this.createUserEntity(request))
        return this.makeUserResponseViewFromUser(user)
    }

    /**
     * 상태 변경
     */
    override fun updateUserStatus(id: Long, status: UserStatus): UserResponseView {
        val user: User = this.findByIdOrThrow(id)

        user.changeStatus(status)
        return this.makeUserResponseViewFromUser(user)
    }


    /**
     * 등급 변경
     */
    override fun updateUserGrade(id: Long, grade: UserGrade): UserResponseView {
        val user: User = this.findByIdOrThrow(id)

        user.changeGrade(grade)
        return this.makeUserResponseViewFromUser(user)
    }

    /**
     * 회원 조회
     */
    override fun getUser(id: Long): UserResponseView {
        val user: User = this.findByIdOrThrow(id)
        if (user.status == UserStatus.DELETED) {
            throw InvalidUserStatusException()
        }

        return makeUserResponseViewFromUser(user)
    }

    /**
     * 회원 정보 수정
     */
    override fun updateUserInfo(id: Long, request: UserUpdateRequestCommand): UserResponseView? {
        val user: User = this.findByIdOrThrow(id)

        user.updateUserInfo(request.userName, request.password, request.mobile, request.address)
        return makeUserResponseViewFromUser(user)
    }

    /**
     * 회원 삭제
     */
    override fun deleteUser(id: Long): UserResponseView? {
        val user: User = this.findByIdOrThrow(id)

        user.delete()
        return makeUserResponseViewFromUser(user)
    }

    /**
     * 이메일 중복 체크
     */
    private fun checkDuplicateEmail(email: String) {
        if (this.userRepository.findByEmail(email) != null) {
            throw DuplicateUserException()
        }
    }

    private fun findByIdOrThrow(id: Long): User {
        return this.userRepository.findById(id)
            .orElseThrow { NotFoundUserException() }
    }

    fun createUserEntity(request: UserCreateRequestCommand): User {
        return User(
            userName = request.userName,
            email = request.email,
            mobile = request.mobile,
            password = request.password,
            address = request.address
        )
    }

    fun makeUserResponseViewFromUser(user: User): UserResponseView {
        user.id?.let { id ->
            return UserResponseView(
                id = id,
                userName = user.userName,
                email = user.email,
                mobile = user.mobile,
                address = user.address,
                status = user.status,
                grade = user.grade
            )
        }
        throw Exception("id is null")
    }
}
