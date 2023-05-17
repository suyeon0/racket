package com.racket.api.user

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.racket.api.user.domain.Address
import com.racket.api.user.domain.UserGrade
import com.racket.api.user.domain.UserStatus
import com.racket.api.user.exception.DuplicateUserException
import com.racket.api.user.exception.InvalidUserStatusException
import com.racket.api.user.request.UserCreateRequestCommand
import com.racket.api.user.request.UserUpdateRequestCommand
import com.racket.api.user.response.UserResponseView
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserControllerTest @Autowired constructor(private val mockMvc: MockMvc) {

    val objectMapper = jacksonObjectMapper()

    @Autowired lateinit var userService: UserService

    // 변경 및 삭제할 유저 데이터 생성
    private fun saveTestUserAndReturnResponseView(): UserResponseView {
        val userCreateRequestCommand = UserCreateRequestCommand(
            userName = "tdd_user",
            email = "tdd_user@naver.com",
            password = "1234567",
            mobile = "01012341234",
            address = Address (
                zipCode = "12345",
                street = "Positano SA",
                detailedAddress = "30"
            )
        )
        return this.userService.registerUser(userCreateRequestCommand)
    }
    @Test
    fun `User Test - 유저를 생성한다 유저 생성이 완료되면 HttpStatus 201이 나와야 하며 DB 에 존재해야 한다`() {
        // given
        val userCreateRequestCommand = UserCreateRequestCommand(
            userName = "zibri",
            email = "test@naver.com",
            password = "1234567",
            mobile = "01012341234",
            address = Address (
                zipCode = "12345",
                street = "Positano SA",
                detailedAddress = "30"
            )
        )

        // when
        val sut = this.mockMvc.post("/user") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(userCreateRequestCommand)
        }.andExpect {
            status {isCreated()}
        }.andReturn()

        // then
        val resultView = objectMapper.readValue(sut.response.contentAsString, UserResponseView::class.java)
        Assertions.assertNotNull(resultView.id)
        Assertions.assertEquals(userCreateRequestCommand.userName, resultView.userName)
        Assertions.assertEquals(userCreateRequestCommand.email, resultView.email)
        Assertions.assertEquals(userCreateRequestCommand.mobile, resultView.mobile)
        Assertions.assertEquals(userCreateRequestCommand.address, resultView.address)
    }


    @Test
    fun `User Test - 유저를 생성할 때 필수값을 입력하지 않는다면 HttpStatus 400 오류가 발생한다`() {
        // given
        val userCreateRequestCommand = UserCreateRequestCommand(
            userName = "",
            email = "test@naver.com",
            password = "1234567",
            mobile = "01012341234",
            address = Address (
                zipCode = "12345",
                street = "Positano SA",
                detailedAddress = "30"
            )
        )

        // when-then
        val sut = this.mockMvc.post("/user") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(userCreateRequestCommand)
        }.andExpect {
            status {isBadRequest()}
        }.andReturn()

    }

    @Test
    fun `User Test - 동일한 email 주소가 있는지 체크한다 동일한 주소가 있으면 HttpStatus 400 & DuplicateUserException 발생`() {
        // given
        val existedEmail = this.saveTestUserAndReturnResponseView().email
        val userCreateRequestCommand = UserCreateRequestCommand(
            email = existedEmail,
            userName = "zibri",
            password = "1234567",
            mobile = "01012341234",
            address = Address (
                zipCode = "12345",
                street = "Positano SA",
                detailedAddress = "30"
            )
        )

        // when-then
        val sut = this.mockMvc.post("/user") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(userCreateRequestCommand)
        }.andExpect {
            status { isBadRequest() }
        }.andReturn()

        val resolvedException = sut.resolvedException
        assert(resolvedException is DuplicateUserException)

    }

    @Test
    fun `User Test - 유저 조회에 사용할 id 포맷이 일치하지 않으면 400 오류` () {
        // given
        val userId = this.saveTestUserAndReturnResponseView().id
        val invalidId = userId.toString() + "ID"

        // when-then
        this.mockMvc.get("/user/{id}", invalidId) {
        }.andExpect {
            status { isBadRequest() }
        }.andReturn()

    }

    @Test
    fun `User Test - 유저 상태를 변경할 수 있어야 한다 상태는 ACTIVE, INACTIVE, DELETED 로 나뉜다 기본값은 ACTIVE 이다`() {
        // given
        val res: UserResponseView = this.saveTestUserAndReturnResponseView() // 변경할 유저 데이터 저장
        val defaultStatus: UserStatus = res.status // 유저 상태 기본값 확인

        val updateUserId = res.id
        val updateStatus = UserStatus.INACTIVE

        // when
        val sut = mockMvc.patch("/user/{id}/status", updateUserId) {
            param("status", updateStatus.name)
        }.andExpect {
            status { isOk() }
        }.andReturn()

        // then
        val resultView = objectMapper.readValue(sut.response.contentAsString, UserResponseView::class.java)
        Assertions.assertEquals(UserStatus.ACTIVE, defaultStatus) // 기본값이 ACTIVE 인지 확인
        Assertions.assertEquals(updateStatus, resultView.status)  // 상태값 변경 확인
    }

    @Test
    fun `User Test - 최초에 회원을 가입하면 일반 유저 등급이 된다 유저 등급은 USER, ADMIN 으로 나뉜다 유저 정보를 불러올 때 해당 유저 등급과 상태를 확인할 수 있어야 한다`() {
        // given
        val res: UserResponseView = this.saveTestUserAndReturnResponseView() // 변경할 유저 데이터 저장
        val defaultGrade: UserGrade = res.grade // 최초 가입시 유저 등급
        val userId = res.id // 정보 조회할 유저 id

        // when
        val sut = this.mockMvc.get("/user/{id}", userId) {
        }.andExpect {
            status { isOk() }
        }.andReturn()

        // then
        // 최초에 회원을 가입하면 일반 유저 등급이 된다
        Assertions.assertEquals(UserGrade.USER, defaultGrade)
        // 유저 등급은 USER, ADMIN 으로 나뉜다
        val list: Array<UserGrade> = UserGrade.values()
        Assertions.assertTrue(UserGrade.USER in list && UserGrade.ADMIN in list)
        // 유저 정보를 불러올 때 해당 유저 등급과 상태를 확인할 수 있어야 한다
        val resultView = objectMapper.readValue(sut.response.contentAsString, UserResponseView::class.java)
        Assertions.assertEquals(UserGrade.USER, resultView.grade)
        Assertions.assertEquals(UserStatus.ACTIVE, resultView.status)
    }

    @Test
    fun `User Test - 유저 등급을 변경할 수 있어야 한다 상태는 USER, ADMIN 으로 나뉜다`() {
        // given
        val res: UserResponseView = this.saveTestUserAndReturnResponseView() // 변경할 유저 데이터 저장

        val updateUserId = res.id
        val updateGrade = UserGrade.ADMIN

        // when
        val sut = mockMvc.patch("/user/{id}/grade", updateUserId) {
            param("grade", updateGrade.name)
        }.andExpect {
            status { isOk() }
        }.andReturn()

        // then
        val resultView = objectMapper.readValue(sut.response.contentAsString, UserResponseView::class.java)
        Assertions.assertEquals(updateGrade, resultView.grade)
    }

    @Test
    fun `User Test - 유저 등급은 USER, ADMIN 으로 나뉜다 다른 등급으로 변경 요청시 HttpStatus 400 오류가 발생한다`() {
        // given
        val res: UserResponseView = this.saveTestUserAndReturnResponseView() // 변경할 유저 데이터 저장

        val updateUserId = res.id
        val invalidGrade = "INVALID_GRADE"

        // when
        mockMvc.patch("/user/{id}/grade", updateUserId) {
            param("grade", invalidGrade)
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
            password = "updatePWD",
            mobile = "01043214321",
            address = Address (
                zipCode = "",
                street = "Positano SA 2",
                detailedAddress = "31"
            )
        )

        // when
        val sut = this.mockMvc.put("/user/{id}/info", userId) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(userUpdateRequestCommand)
        }.andExpect {
            status { isOk() }
        }.andReturn()

        // then
        val resultView = objectMapper.readValue(sut.response.contentAsString, UserResponseView::class.java)
        Assertions.assertEquals(userUpdateRequestCommand.userName, resultView.userName)
        Assertions.assertEquals(userUpdateRequestCommand.mobile, resultView.mobile)
        Assertions.assertEquals(userUpdateRequestCommand.address, resultView.address)
    }

    @Test
    fun `User Test - 유저 정보를 삭제할 수 있어야 한다 삭제된 유저는 DELETED 상태로 변경되고 조회할 수 없다`() {
        // given
        val res: UserResponseView = this.saveTestUserAndReturnResponseView() // 삭제 대상 유저 데이터 저장
        val deleteUserId = res.id

        // when
        val sut = this.mockMvc.delete("/user/{id}", deleteUserId)
            .andExpect {
                status { isOk() }
            }.andReturn()

        // then
        // DELETED 상태로 변경
        val resultView = objectMapper.readValue(sut.response.contentAsString, UserResponseView::class.java)
        Assertions.assertEquals(UserStatus.DELETED, resultView.status)

        // 조회할 수 없어야 한다
        val sutGet = this.mockMvc.get("/user/{id}", deleteUserId)
                .andExpect {
                    status { isBadRequest() }
                }.andReturn()
        val resolvedException = sutGet.resolvedException
        assert(resolvedException is InvalidUserStatusException)
    }


}