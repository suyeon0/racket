package com.racket.api.user

import com.racket.api.shared.vo.AddressVO
import com.racket.api.user.domain.User
import com.racket.api.user.enums.UserRoleType
import com.racket.api.user.domain.UserRepository
import com.racket.api.user.vo.UserSignedUpEventVO
import com.racket.api.user.enums.UserStatusType
import com.racket.api.user.exception.DuplicateUserException
import com.racket.api.user.exception.InvalidUserStatusException
import com.racket.api.user.exception.NotFoundUserException
import com.racket.api.user.request.UserUpdateRequestCommand
import com.racket.api.user.response.UserAdditionalResponseView
import com.racket.api.user.response.UserResponseView
import mu.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val eventPublisher: ApplicationEventPublisher
)
    : UserService {

    private val log = KotlinLogging.logger { }

    /**
     * 회원 등록
     */
    @Transactional
    override fun registerUser(userRegisterDTO: UserService.UserRegisterDTO): UserResponseView {
        if (this.isExistDuplicatedEmail(userRegisterDTO.email)) {
            throw DuplicateUserException()
        }

        val user = this.userRepository.save(this.createUserEntity(userRegisterDTO))

        // 회원 가입 완료 비동기 이벤트 발행
        this.eventPublisher.publishEvent(
            UserSignedUpEventVO(
                userName = user.userName,
                userEmail = user.email,
                userId = user.id!!,
                userMobileNumber = user.mobileVO!!.number)
        )

        // TODO: 회원 가입 쿠폰 발행

        return this.makeUserResponseViewFromUser(user)
    }

    /**
     * 상태 변경
     */
    override fun updateUserStatus(id: Long, status: UserStatusType) =
        this.makeUserResponseViewFromUser(this.getUserEntity(id).updateStatus(status))

    /**
     * 등급 변경
     */
    override fun updateUserRole(id: Long, role: UserRoleType) =
        this.makeUserResponseViewFromUser(this.getUserEntity(id).updateRole(role))

    /**
     * 회원 조회
     */
    override fun getUser(id: Long): UserResponseView {
//        val user = try {
//            this.getUserFromCache(id)
//        } catch (dataNotFoundException: DataNotFoundException) {
//           this.getUserEntity(id)
//        }

        val user =  this.getUserEntity(id)

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
        this.makeUserResponseViewFromUser(this.getUserEntity(id).updateStatus(UserStatusType.DELETED))

    /**
     * 회원 추가 정보 등록
     */
    override fun registerAdditionalUserInformation(id: Long, addressVO: AddressVO?) =
        this.makeUserAdditionalResponseViewFromUser(
            this.getUserEntity(id)
                .updateUserAdditionalInfo(
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
            mobileVO = registerDTO.mobileVO,
            addressVO = null
        )

    fun makeUserResponseViewFromUser(user: User) =
        UserResponseView(
            id = user.id!!,
            userName = user.userName,
            email = user.email,
            status = user.status,
            role = user.role,
            password = user.password,
            mobile = user.mobileVO!!
        )

    fun makeUserAdditionalResponseViewFromUser(user: User) =
        UserAdditionalResponseView(
            id = user.id!!,
            addressVO = user.addressVO
        )

}
