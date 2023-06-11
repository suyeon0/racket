package com.racket.api.user

import com.racket.api.auth.login.response.LoginUserResponseView
import com.racket.api.common.vo.AddressVO
import com.racket.api.common.vo.MobileVO
import com.racket.api.user.domain.User
import com.racket.api.user.domain.UserRole
import com.racket.api.user.domain.UserRepository
import com.racket.api.user.domain.UserStatus
import com.racket.api.user.exception.DuplicateUserException
import com.racket.api.user.exception.InvalidUserStatusException
import com.racket.api.user.exception.NotFoundUserException
import com.racket.api.user.request.UserUpdateRequestCommand
import com.racket.api.user.response.UserAdditionalResponseView
import com.racket.api.user.response.UserResponseView
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    /**
     * 회원 등록
     */
    override fun registerUser(userRegisterDTO: UserService.UserRegisterDTO): UserResponseView {
        if (this.isExistDuplicatedEmail(userRegisterDTO.email)) {
            throw DuplicateUserException()
        }

        val user = this.userRepository.save(this.createUserEntity(userRegisterDTO))
        return this.makeUserResponseViewFromUser(user)
    }

    /**
     * 상태 변경
     */
    override fun updateUserStatus(id: Long, status: UserStatus) =
        this.makeUserResponseViewFromUser(this.getUserEntity(id).updateStatus(status))

    /**
     * 등급 변경
     */
    override fun updateUserRole(id: Long, role: UserRole) =
        this.makeUserResponseViewFromUser(this.getUserEntity(id).updateRole(role))

    /**
     * 회원 조회
     */
    override fun getUser(id: Long): UserResponseView {
        val user = this.getUserEntity(id)

        if (user.isDeletedStatus()) {
            throw InvalidUserStatusException()
        } else {
            return this.makeUserResponseViewFromUser(user)
        }
    }

    /**
     * 회원 정보 수정
     */
    override fun updateUserInfo(id: Long, request: UserUpdateRequestCommand) =
        this.makeUserResponseViewFromUser(
            this.getUserEntity(id)
                .updateUserInfo(
                    userName = request.userName,
                    password = request.password
                )
        )

    /**
     * 회원 삭제
     */
    override fun deleteUser(id: Long) =
        this.makeUserResponseViewFromUser(this.getUserEntity(id).updateStatus(UserStatus.DELETED))

    /**
     * 회원 추가 정보 등록
     */
    override fun registerAdditionalUserInformation(id: Long, mobileVO: MobileVO?, addressVO: AddressVO?) =
        this.makeUserAdditionalResponseViewFromUser(
            this.getUserEntity(id)
                .updateUserAdditionalInfo(
                    mobileVO = mobileVO,
                    addressVO = addressVO
                )
        )

    override fun getUserByEmail(email: String): UserResponseView {
        val optUser = this.userRepository.findByEmail(email)
        if (optUser.isPresent) {
            return this.makeUserResponseViewFromUser(optUser.get())
        } else {
            throw NotFoundUserException()
        }
    }

    override fun getUserByEmailAndPassword(email: String, password: String): Optional<User> =
        this.userRepository.findByEmailAndPassword(email = email, password = password)


    /**
     * 이메일 중복 체크
     */
    private fun isExistDuplicatedEmail(email: String) = this.userRepository.findByEmail(email).isPresent

    private fun getUserEntity(id: Long) =
        this.userRepository.findById(id).orElseThrow { NotFoundUserException() }

    fun createUserEntity(registerDTO: UserService.UserRegisterDTO) =
        User(
            userName = registerDTO.userName,
            password = registerDTO.password,
            email = registerDTO.email,
            mobileVO = null,
            addressVO = null
        )

    fun makeUserResponseViewFromUser(user: User) =
        UserResponseView(
            id = user.id!!,
            userName = user.userName,
            email = user.email,
            status = user.status,
            role = user.role,
            password = user.password
        )

    fun makeUserAdditionalResponseViewFromUser(user: User) =
        UserAdditionalResponseView(
            id = user.id!!,
            mobileVO = user.mobileVO,
            addressVO = user.addressVO
        )

}
