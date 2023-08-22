package com.racket.cash

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.racket.cash.enums.CashEventType
import com.racket.cash.enums.CashTransactionStatusType
import com.racket.cash.exception.InvalidChargingTransactionException
import com.racket.cash.request.CashChargeCommand
import com.racket.cash.response.CashBalanceResponseView
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.event.RecordApplicationEvents
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional

@Transactional
@AutoConfigureMockMvc
@RecordApplicationEvents
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CashControllerTest {

    val objectMapper = jacksonObjectMapper()

    @Autowired
    lateinit var mockMvc: MockMvc

    companion object {
        const val cashRequestURL = "/api/v1/racket-cash"
    }

    /**
     * Consumer 이전
     */
    @Test
    fun `Cash Test - userID로 캐시 충전 총액 조회시 200 status와 데이터가 조회되어야 한다`() {
        // when
        val userId = 1L
        val sut = this.mockMvc.get("${cashRequestURL}/balance/{user_id}", userId) {}
            .andExpect { status { isOk() } }
            .andReturn()

        // then
        val resultView = objectMapper.registerModule(JavaTimeModule())
            .readValue(sut.response.contentAsString, CashBalanceResponseView::class.java)
        Assertions.assertEquals(userId, resultView.userId)
    }

    @Test
    fun `Cash Test - 충전요청시 정해진 충전금액단위와 일치하지 않으면 400 status & InvalidChargingTransactionException 발생`() {
        // given
        val command = CashChargeCommand(
            userId = 28,
            amount = 111111,
            accountId = 1,
            eventType = CashEventType.CHARGING,
            status = CashTransactionStatusType.REQUEST
        )
        // when
        val sut = this.mockMvc.post("${cashRequestURL}/charge/request") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(command)
        }.andExpect {
            status { isBadRequest() }
        }.andReturn()

        // then
        val resolvedException = sut.resolvedException
        assert(resolvedException is InvalidChargingTransactionException)
    }

    @Test
    fun `Cash Test - 충전요청시 계좌정보가 유효하지 않으면 않으면 400 status & InvalidChargingTransactionException 발생`() {
        // given
        val command = CashChargeCommand(
            userId = 28,
            amount = 50000,
            accountId = 1111111,
            eventType = CashEventType.CHARGING,
            status = CashTransactionStatusType.REQUEST
        )
        // when
        val sut = this.mockMvc.post("${cashRequestURL}/charge/request") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(command)
        }.andExpect {
            status { isBadRequest() }
        }.andReturn()

        // then
        val resolvedException = sut.resolvedException
        assert(resolvedException is InvalidChargingTransactionException)
    }

    @Test
    fun `Cash Test - 충전요청이 성공하면, 201 Status를 받고 Request 상태값으로 충전트랜잭션 로그 테이블에 데이터가 생성되어야 한다`() {
        // given
        val command = CashChargeCommand(
            userId = 28,
            amount = 50000,
            accountId = 1,
            eventType = CashEventType.CHARGING,
            status = CashTransactionStatusType.REQUEST
        )
        // when
        val sut = this.mockMvc.post("${cashRequestURL}/charge/request") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(command)
        }.andExpect {
            status { isCreated() }
        }.andReturn()

        // then


    }

    /**
     * Consumer 이후
     */


}