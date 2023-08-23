package com.racket.cash

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.racket.api.user.vo.UserSignedUpEventVO
import com.racket.cash.entity.CashTransaction
import com.racket.cash.enums.CashEventType
import com.racket.cash.enums.CashTransactionStatusType
import com.racket.cash.exception.InsertCashTransactionException
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
import org.mockito.Mockito.*
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
class CashTransactionControllerTest {

    val objectMapper = jacksonObjectMapper()

    @Autowired
    lateinit var mockMvc: MockMvc

    companion object {
        const val cashTransactionRequestURL = "/api/v1/racket-cash/transaction"
    }

    @Test
    fun `Cash Transaction - 캐시 충전 이력 조회 성공시 200 status & userId, amount, status 값을 리턴한다`() {
        // given
        val eventId = "64e56bc8013d855d43711d6e"

        // when
        val sut = this.mockMvc.get("${cashTransactionRequestURL}/{event_id}", eventId) {}
            .andExpect { status { isOk() } }
            .andReturn()

        // then
        val resultView = objectMapper.registerModule(JavaTimeModule())
            .readValue(sut.response.contentAsString, CashTransactionResponseView::class.java)
        Assertions.assertNotNull(resultView.transactionId)
        Assertions.assertNotNull(resultView.userId)
        Assertions.assertNotNull(resultView.amount)
        Assertions.assertNotNull(resultView.status)
    }

    @Test
    fun `Cash Transaction - 캐시 충전 이력 데이터가 없을 때 400 status 리턴한다`() {
        // given
        val eventId = "NONE"

        // when-then
        this.mockMvc.get("${cashTransactionRequestURL}/{event_id}", eventId) {}
            .andExpect { status { isBadRequest() } }
            .andReturn()
    }

    @Test
    fun `Cash Transaction - 충전 트랜잭션 데이터 insert 가 실패하면 400 status & InsertCashTransactionException 발생한다`() {
        // given
        val mockRepository = mock(CashTransactionRepository::class.java)
        val service = CashTransactionLogServiceImpl(mockRepository)
        val chargeVO = ChargeVO(
            transactionId = ObjectId(),
            userId = 1L,
            amount = 50000,
            eventType = CashEventType.CHARGING,
            accountId = 1L,
            status = CashTransactionStatusType.REQUEST
        )

        // 데이터 삽입이 실패하도록 설정
        `when`(mockRepository.insert(chargeVO.makeCashTransactionEntity())).thenThrow(RuntimeException::class.java)

        // when
        Assertions.assertThrows(InsertCashTransactionException::class.java) {
            service.insertChargeTransaction(chargeVO)
        }
    }


}