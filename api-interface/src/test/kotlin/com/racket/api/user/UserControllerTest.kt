package com.racket.api.user

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.racket.api.user.presentation.request.UserAdditionalInfoCreateRequestCommand
import com.racket.api.user.presentation.request.UserCreateRequestCommand
import com.racket.api.user.presentation.request.UserUpdateRequestCommand
import com.racket.api.user.presentation.response.UserAdditionalResponseView
import com.racket.api.user.presentation.response.UserResponseView
import com.racket.api.user.vo.UserSignedUpEventVO
import com.racket.share.domain.user.enums.UserRoleType
import com.racket.share.domain.user.enums.UserStatusType
import com.racket.share.domain.user.exception.DuplicateUserException
import com.racket.share.domain.user.exception.InvalidUserStatusException
import com.racket.share.vo.AddressVO
import com.racket.share.vo.MobileVO
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.event.ApplicationEvents
import org.springframework.test.context.event.RecordApplicationEvents
import org.springframework.test.web.servlet.*
import org.springframework.transaction.annotation.Transactional


@Transactional
@AutoConfigureMockMvc
@RecordApplicationEvents
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var events: ApplicationEvents

    private val objectMapper = jacksonObjectMapper()
    private val userCreateRequestCommand = UserCreateRequestCommand(
        userName = "zibri",
        email = "test@naver.com",
        password = "1234567",
        mobileVO = MobileVO(number = "01012341234")
    )

    // 변경 및 삭제할 유저 데이터 생성
    private fun saveTestUserAndReturnResponseView(): UserResponseView {
        val userRegisterDTO = UserService.UserRegisterDTO(
            userName = "tdd_user",
            email = "tdd_user@naver.com",
            password = "1234567",
            mobileVO = MobileVO(number = "01012341234")
        )
        return this.userService.registerUser(userRegisterDTO)
    }

    @Test
    fun `User Test - 유저를 생성한다 유저 생성이 완료되면 HttpStatus 201이 나와야 하며 DB 에 존재해야 한다`() {
        // given

        // when
        val sut = this.mockMvc.post("/api/v1/user") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(userCreateRequestCommand)
        }.andExpect {
            status { isCreated() }
        }.andReturn()

        // then
        val resultView = objectMapper.registerModule(JavaTimeModule()).readValue(sut.response.contentAsString, UserResponseView::class.java)

        Assertions.assertNotNull(resultView.id)
        Assertions.assertEquals(userCreateRequestCommand.userName, resultView.userName)
        Assertions.assertEquals(userCreateRequestCommand.email, resultView.email)
    }


    @Test
    fun `User Test - 유저를 생성할 때 필수값을 입력하지 않는다면 HttpStatus 400 오류가 발생한다`() {
        // given
        val invalidUserCreateRequestCommand = UserCreateRequestCommand(
            userName = "",
            email = "test@naver.com",
            password = "1234567",
            mobileVO = MobileVO("01012341234")
        )

        // when-then
        this.mockMvc.post("/api/v1/user") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(invalidUserCreateRequestCommand)
        }.andExpect {
            status { isBadRequest() }
        }.andReturn()

    }

    @Test
    fun `User Test - 동일한 email 주소가 있는지 체크한다 동일한 주소가 있으면 HttpStatus 400 & DuplicateUserException 발생`() {
        // given
        val existedEmail = this.saveTestUserAndReturnResponseView().email
        val duplicatedUserCreateRequestCommand = UserCreateRequestCommand(
            userName = "existedEmailTestName",
            email = existedEmail,
            password = "1234567",
            mobileVO = MobileVO(number = "01012341234")
        )

        // when-then
        val sut = this.mockMvc.post("/api/v1/user") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(duplicatedUserCreateRequestCommand)
        }.andExpect {
            status { isBadRequest() }
        }.andReturn()

        val resolvedException = sut.resolvedException
        assert(resolvedException is DuplicateUserException)
    }

    @Test
    fun `User Test - 유저 조회에 사용할 id 포맷이 일치하지 않으면 400 오류`() {
        // given
        val userId = this.saveTestUserAndReturnResponseView().id
        val invalidId = userId.toString() + "ID"

        // when-then
        this.mockMvc.get("/api/v1/user/{id}", invalidId) {
        }.andExpect {
            status { isBadRequest() }
        }.andReturn()

    }

    @ParameterizedTest
    @EnumSource(value = UserStatusType::class, names = ["ACTIVE", "INACTIVE", "DELETED"])
    fun `User Test - 유저 상태를 변경할 수 있어야 한다 상태는 ACTIVE, INACTIVE, DELETED 로 나뉜다 기본값은 ACTIVE 이다`(updateStatus: UserStatusType) {
        // given
        val res: UserResponseView = this.saveTestUserAndReturnResponseView() // 변경할 유저 데이터 저장
        val defaultStatus: UserStatusType = res.status // 유저 상태 기본값 확인

        val updateUserId = res.id

        // when
        val sut = mockMvc.patch("/api/v1/user/{id}/status", updateUserId) {
            param("status", updateStatus.name)
        }.andExpect {
            status { isOk() }
        }.andReturn()

        // then
        val resultView = objectMapper.registerModule(JavaTimeModule()).readValue(sut.response.contentAsString, UserResponseView::class.java)
        Assertions.assertEquals(UserStatusType.ACTIVE, defaultStatus) // 기본값이 ACTIVE 인지 확인
        Assertions.assertEquals(updateStatus, resultView.status)  // 상태값 변경 확인
    }

    @Test
    fun `User Test - 최초에 회원을 가입하면 일반 유저 등급이 된다 유저 등급은 USER, ADMIN 으로 나뉜다 유저 정보를 불러올 때 해당 유저 등급과 상태를 확인할 수 있어야 한다`() {
        // given
        val res: UserResponseView = this.saveTestUserAndReturnResponseView() // 변경할 유저 데이터 저장
        val defaultRole: UserRoleType = res.role // 최초 가입시 유저 등급
        val userId = res.id // 정보 조회할 유저 id

        // when
        val sut = this.mockMvc.get("/api/v1/user/{id}", userId) {
        }.andExpect {
            status { isOk() }
        }.andReturn()

        // then
        // 최초에 회원을 가입하면 일반 유저 등급이 된다
        Assertions.assertEquals(UserRoleType.USER, defaultRole)
        // 유저 등급은 USER, ADMIN 으로 나뉜다
        val list: Array<UserRoleType> = UserRoleType.values()
        Assertions.assertTrue(UserRoleType.USER in list && UserRoleType.ADMIN in list)
        // 유저 정보를 불러올 때 해당 유저 등급과 상태를 확인할 수 있어야 한다
        val resultView = objectMapper.registerModule(JavaTimeModule()).readValue(sut.response.contentAsString, UserResponseView::class.java)
        Assertions.assertEquals(UserRoleType.USER, resultView.role)
        Assertions.assertEquals(UserStatusType.ACTIVE, resultView.status)
    }

    @Test
    fun `User Test - 유저 등급을 변경할 수 있어야 한다 상태는 USER, ADMIN 으로 나뉜다`() {
        // given
        val res: UserResponseView = this.saveTestUserAndReturnResponseView() // 변경할 유저 데이터 저장

        val updateUserId = res.id
        val updateRole = UserRoleType.ADMIN

        // when
        val sut = mockMvc.patch("/api/v1/user/{id}/role", updateUserId) {
            param("role", updateRole.name)
        }.andExpect {
            status { isOk() }
        }.andReturn()

        // then
        val resultView = objectMapper.registerModule(JavaTimeModule()).readValue(sut.response.contentAsString, UserResponseView::class.java)
        Assertions.assertEquals(updateRole, resultView.role)
    }

    @Test
    fun `User Test - 유저 등급은 USER, ADMIN 으로 나뉜다 다른 등급으로 변경 요청시 HttpStatus 400 오류가 발생한다`() {
        // given
        val res: UserResponseView = this.saveTestUserAndReturnResponseView() // 변경할 유저 데이터 저장

        val updateUserId = res.id
        val invalidRole = "INVALID_ROLE"

        // when
        mockMvc.patch("/api/v1/user/{id}/role", updateUserId) {
            param("role", invalidRole)
        }.andExpect {
            status { isBadRequest() }
        }

    }


    @Test
    fun `User Test - 유저 정보를 수정할 수 있어야 한다 단 email 주소는 고유한 값이기 때문에 수정할 수 없다`() {
        // given
        val res: UserResponseView = this.saveTestUserAndReturnResponseView() // 변경할 유저 데이터 저장
        val userId = res.id // 변경할 유저 id
        val userUpdateRequestCommand = UserUpdateRequestCommand(
            userName = "updateUser",
            password = "updatePWD"
        )

        // when
        val sut = this.mockMvc.patch("/api/v1/user/{id}/info", userId) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(userUpdateRequestCommand)
        }.andExpect {
            status { isOk() }
        }.andReturn()

        // then
        val resultView = objectMapper.registerModule(JavaTimeModule()).readValue(sut.response.contentAsString, UserResponseView::class.java)
        Assertions.assertEquals(userUpdateRequestCommand.userName, resultView.userName)
    }

    @Test
    fun `User Test - 유저 정보를 삭제할 수 있어야 한다 삭제된 유저는 DELETED 상태로 변경되고 조회할 수 없다`() {
        // given
        val res: UserResponseView = this.saveTestUserAndReturnResponseView() // 삭제 대상 유저 데이터 저장
        val deleteUserId = res.id

        // when
        val sut = this.mockMvc.delete("/api/v1/user/{id}", deleteUserId)
            .andExpect {
                status { isOk() }
            }.andReturn()

        // then
        // DELETED 상태로 변경
        val resultView = objectMapper.registerModule(JavaTimeModule()).readValue(sut.response.contentAsString, UserResponseView::class.java)
        Assertions.assertEquals(UserStatusType.DELETED, resultView.status)

        // 조회할 수 없어야 한다
        val sutGet = this.mockMvc.get("/api/v1/user/{id}", deleteUserId)
            .andExpect {
                status { isUnauthorized() }
            }.andReturn()
        val resolvedException = sutGet.resolvedException
        assert(resolvedException is InvalidUserStatusException)
    }

    @Test
    fun `User Test - 유저 추가 정보를 정상적으로 저장하면 200 을 리턴 받는다`() {
        // given
        val res: UserResponseView = this.saveTestUserAndReturnResponseView() // 변경할 유저 데이터 저장
        val userId = res.id // 저장할 유저 id
        val userAdditionalInfoCreateRequestCommand = UserAdditionalInfoCreateRequestCommand(
            addressVO = AddressVO (
                zipCode = "",
                street = "Positano SA 2",
                detailedAddress = "31"
            )
        )

        // when
        val sut = this.mockMvc.patch("/api/v1/user/{id}/additional-info", userId) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(userAdditionalInfoCreateRequestCommand)
        }.andExpect {
            status { isOk() }
        }.andReturn()

        // then
        val resultView = objectMapper.registerModule(JavaTimeModule()).readValue(sut.response.contentAsString, UserAdditionalResponseView::class.java)
        val addressResult = resultView.addressVO!!
        Assertions.assertEquals(userAdditionalInfoCreateRequestCommand.addressVO.zipCode, addressResult.zipCode)
        Assertions.assertEquals(userAdditionalInfoCreateRequestCommand.addressVO.detailedAddress, addressResult.detailedAddress)
        Assertions.assertEquals(userAdditionalInfoCreateRequestCommand.addressVO.street, addressResult.street)
    }

    @Test
    fun `User Test - 유저 정보 저장시 핸드폰 번호 포맷이 불일치하면 400 오류를 리턴받는다`() {
        // given
        val invalidUserCreateRequestCommand = UserCreateRequestCommand(
            userName = "invalidUserCreateRequestCommand",
            email = "test@naver.com",
            password = "1234567",
            mobileVO = MobileVO(number = "invalidNumber")
        )

        // when - then
        this.mockMvc.post("/api/v1/user", invalidUserCreateRequestCommand) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(invalidUserCreateRequestCommand)
        }.andExpect {
            status { isBadRequest() }
        }.andReturn()
    }

    @Test
    fun `User Test - 회원가입이 트랜잭션이 커밋되면 가입 완료 메일 전송 이벤트가 1회 발생되어야 한다`() {
        // given

        // when
        // 회원가입 진행
        this.mockMvc.post("/api/v1/user") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(userCreateRequestCommand)
        }.andExpect {
            status { isCreated() }
        }

        // then
        val count = events.stream(UserSignedUpEventVO::class.java).count()
        Assertions.assertEquals(1, count)
    }
}