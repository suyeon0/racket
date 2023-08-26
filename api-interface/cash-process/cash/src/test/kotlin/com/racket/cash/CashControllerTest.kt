package com.racket.cash

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.racket.cash.enums.CashEventType
import com.racket.cash.enums.CashTransactionStatusType
import com.racket.cash.events.ChargingProduceEventVO
import com.racket.cash.exception.InvalidChargingTransactionException
import com.racket.cash.repository.CashTransactionRepository
import com.racket.cash.request.CashChargeCommand
import com.racket.cash.response.CashBalanceResponseView
import com.racket.cash.response.CashTransactionResponseView
import com.racket.cash.response.ChargeResponseView
import com.racket.cash.vo.ChargeVO
import com.racket.cash.vo.makeCashTransactionEntity
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.event.ApplicationEvents
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
    companion object {
        const val cashRequestURL = "/api/v1/racket-cash"
    }

    val objectMapper = jacksonObjectMapper()
    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var events: ApplicationEvents


    @Test
    fun `Cash - userID로 캐시 충전 총액 조회시 200 status와 데이터가 조회되어야 한다 캐시 충전 이력이 없으면 0원을 리턴한다`() {
        // when
        val userId = 1L
        val sut = this.mockMvc.get("${cashRequestURL}/balance/{user_id}", userId) {}
            .andExpect { status { isOk() } }
            .andReturn()

        // then
        val resultView = objectMapper.registerModule(JavaTimeModule())
            .readValue(sut.response.contentAsString, CashBalanceResponseView::class.java)
        Assertions.assertEquals(userId, resultView.userId)
        Assertions.assertEquals(0, resultView.balance)
    }

    @Test
    fun `Cash - 충전요청시 정해진 충전금액단위와 일치하지 않으면 400 status & InvalidChargingTransactionException 발생`() {
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
    fun `Cash - 충전요청시 계좌정보가 유효하지 않으면 않으면, 400 status & InvalidChargingTransactionException 발생`() {
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
    fun `Cash - 충전요청이 성공하면, 201 Status & Request 상태로 충전트랜잭션 로그 테이블에 데이터가 생성되어야 한다`() {
        // given
        val command = CashChargeCommand(
            userId = 28,
            amount = 50000,
            accountId = 1,
            eventType = CashEventType.CHARGING,
            status = CashTransactionStatusType.REQUEST
        )

        // when - then
        // (1) 201 Status
        val sut = this.mockMvc.post("${cashRequestURL}/charge/request") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(command)
        }.andExpect {
            status { isCreated() }
        }.andReturn()

        // (2) 충전트랜잭션 로그 데이터 생성
        val eventId =
            objectMapper.registerModule(JavaTimeModule()).readValue(sut.response.contentAsString, ChargeResponseView::class.java)
                .id
        val savedData = this.mockMvc.get("${CashTransactionControllerTest.cashTransactionRequestURL}/{event_id}", eventId) {}
            .andExpect { status { isOk() } }
            .andReturn()
        val resultView = objectMapper.registerModule(JavaTimeModule())
            .readValue(savedData.response.contentAsString, CashTransactionResponseView::class.java)
        Assertions.assertEquals(resultView.status, CashTransactionStatusType.REQUEST)
    }

    @Test
    fun `Cash - 충전요청이 성공하면, Produce Event 가 발생되어야 한다`() {
        // given
        val command = CashChargeCommand(
            userId = 28,
            amount = 50000,
            accountId = 1,
            eventType = CashEventType.CHARGING,
            status = CashTransactionStatusType.REQUEST
        )

        // when
        this.mockMvc.post("${cashRequestURL}/charge/request") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(command)
        }.andExpect {
            status { isCreated() }
        }

        // then
        val count = events.stream(ChargingProduceEventVO::class.java).count()
        Assertions.assertEquals(1, count)
    }


}