package com.racket.api.auth.login

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.racket.api.auth.login.exception.LoginFailException
import com.racket.api.auth.login.exception.NoSuchSessionException
import com.racket.api.auth.login.filter.LoginCheckFilter
import com.racket.api.auth.login.presentation.request.LoginRequestCommand
import com.racket.api.auth.login.presentation.response.LoginUserResponseView
import com.racket.api.auth.login.session.SessionManager
import com.racket.api.auth.login.session.SessionRedisManager
import com.racket.api.auth.login.session.domain.SessionUser
import com.racket.api.auth.login.session.domain.SessionRedisRepository
import com.racket.api.user.UserService
import com.racket.api.user.presentation.response.UserResponseView
import com.racket.share.vo.MobileVO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Import(EmbeddedRedisConfig::class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class LoginControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var sessionRedisRepository: SessionRedisRepository

    private val objectMapper = jacksonObjectMapper()

    private val mockSessionManger: SessionManager = mock(SessionManager::class.java)


    // 로그인 할 유저 데이터 H2에 생성
    private fun saveTestUserAndReturnResponseView(): UserResponseView {
        val userRegisterDTO = UserService.UserRegisterDTO(
            userName = "tdd_login_user",
            email = "tdd_login_user@naver.com",
            password = "123456",
            mobileVO = MobileVO(number = "0101234124")
        )
        return this.userService.registerUser(userRegisterDTO)
    }

    @Test
    fun `Redis Test - redis 단위 테스트 - session 정보가 저장되고 조회되어야 한다`() {
        // given
        val sessionUser = SessionUser(
            sessionId = UUID.randomUUID().toString(),
            expireTime = LocalDateTime.now().plusMinutes(1),
            userId = 1L,
            name = "sessionUser",
            role = "USER",
            email = "sessionUser@naver.com"
        )
        this.sessionRedisRepository.save(sessionUser)

        // when
        val sut = this.sessionRedisRepository.findById(sessionUser.sessionId).orElseThrow()

        // then
        Assertions.assertEquals(sessionUser.sessionId, sut.sessionId)
        Assertions.assertEquals(sessionUser.userId, sut.userId)
    }

    @Test
    fun `Login Test - 이메일과 패스워드가 일치하여 로그인에 성공하면, 200 성공 및 DB에 저장된 유저 정보가 있는 response 를 받아야 한다`() {
        // given
        val savedUser = this.saveTestUserAndReturnResponseView()
        val loginRequestCommand = LoginRequestCommand(email = savedUser.email, password = savedUser.password)

        // when
        val sut = this.mockMvc.post("/api/v1/auth/login") {
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(loginRequestCommand)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }.andReturn()

        // then
        val resultView = objectMapper.readValue(sut.response.contentAsString, LoginUserResponseView::class.java)
        Assertions.assertEquals(savedUser.userName, resultView.user.name)
        Assertions.assertEquals(savedUser.email, resultView.user.email)
    }


    @Test
    fun `Login Test - 이메일 또는 패스워드가 불일치하여 로그인에 실패하면, 400 status & LoginFailException 이 떨어져야 한다`() {
        // given
        val savedUser = this.saveTestUserAndReturnResponseView()
        val loginRequestCommand = LoginRequestCommand(email = savedUser.email, password = savedUser.password + "@@@")

        // when-then
        val sut = this.mockMvc.post("/api/v1/auth/login") {
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(loginRequestCommand)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
        }.andReturn()

        val resolvedException = sut.resolvedException
        assert(resolvedException is LoginFailException)
    }

    @Test
    fun `Logout Test - 로그아웃하면 home 으로 redirect 되고 redis 에서 세션 데이터가 삭제되어야 한다`() {
        // given
        // login session db 저장
        val sessionId = UUID.randomUUID().toString()
        this.sessionRedisRepository.save(
            SessionUser(
                sessionId = sessionId,
                expireTime = LocalDateTime.now().plusMinutes(10),
                userId = 1L,
                name = "name",
                role = "role",
                email = "email"
            )
        )
        // 세팅할 쿠키 생성
        val sessionCookie = Cookie(SessionRedisManager.SESSION_COOKIE, sessionId)

        // when-then
        // home 으로 redirect
        this.mockMvc.get("/view/v1/auth/logout") {
            cookie(sessionCookie)
        }.andExpect {
            status { is3xxRedirection() }
            redirectedUrl("/")
        }
        // redis 에서 세션 데이터가 삭제되어야 한다
        val sessionOpt = this.sessionRedisRepository.findById(sessionId)
        Assertions.assertTrue(sessionOpt.isEmpty)
    }

    @Test
    fun `LoginFilter Test - 로그인 하지 않고 user-info view 에 접근시, loginForm 으로 redirect 되어야 한다`() {
        // given
        val mockReq = MockHttpServletRequest().apply {
            requestURI = "/view/v1/auth/user-info"
        }
        val mockRes = MockHttpServletResponse()
        val mockFilterChain = MockFilterChain()
        val loginCheckFilter = LoginCheckFilter(mockSessionManger)

        // when
        `when`(this.mockSessionManger.getSessionBySessionCookie(mockReq)).thenThrow(NoSuchSessionException())
        loginCheckFilter.doFilter(mockReq, mockRes, mockFilterChain)

        // then
        Assertions.assertEquals("/view/auth/login", mockRes.redirectedUrl)
    }

    @Test
    fun `LoginFilter Test - 로그인 이후 user-info view 에 접근시, 해당 url 접근이 가능해야 한다`() {
        // given
        val mockReq: HttpServletRequest = MockHttpServletRequest()
        val mockRes: HttpServletResponse = MockHttpServletResponse()
        val mockFilterChain: FilterChain = MockFilterChain()
        val loginCheckFilter = LoginCheckFilter(mockSessionManger)

        // 로그인 성공
        val savedUser = this.saveTestUserAndReturnResponseView()
        val loginRequestCommand = LoginRequestCommand(email = savedUser.email, password = savedUser.password)
        this.mockMvc.post("/api/v1/auth/login") {
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(loginRequestCommand)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }.andReturn()

        // when-then
        this.mockMvc.get("/view/v1/auth/user-info") {
            loginCheckFilter.doFilter(mockReq, mockRes, mockFilterChain)
        }.andExpect {
            status { isOk() }
        }
    }


}