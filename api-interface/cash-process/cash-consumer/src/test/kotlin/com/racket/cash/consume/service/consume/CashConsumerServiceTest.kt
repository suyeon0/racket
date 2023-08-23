package com.racket.cash.consume.service.consume

import com.racket.api.payment.presentation.PaymentErrorCodeConstants
import com.racket.api.payment.presentation.RetryPaymentCallRequiredException
import com.racket.api.payment.presentation.response.PaymentApiResponse
import com.racket.cash.consume.client.CashFeignClient
import com.racket.cash.consume.service.PaymentCallServiceImpl
import com.racket.cash.enums.CashEventType
import com.racket.cash.enums.CashTransactionStatusType
import com.racket.cash.exception.CashApiCallException
import com.racket.cash.exception.ChargePayException
import com.racket.cash.exception.InvalidChargingTransactionException
import com.racket.cash.response.CashTransactionResponseView
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


@SpringBootTest
class CashConsumerServiceTest {

    @MockBean
    private lateinit var cashFeignClient: CashFeignClient

    @MockBean
    private lateinit var paymentCallService: PaymentCallServiceImpl

    companion object {
        private val eventId = ObjectId()
        private val transactionId = ObjectId()
    }

    @Test
    fun `Cash Consumer - cash 토픽 메세지의 value 인 transactionEventId 로 트랜잭션 데이터를 조회한다 데이터 조회 호출이 실패하면 CashApiCallException 발생`() {
        // given
        val cashConsumerService =
            CashConsumerServiceImpl(cashClient = cashFeignClient, paymentCallService = paymentCallService)
        `when`(cashFeignClient.getTransactionList(eventId)).thenThrow(RuntimeException("feign client runtime Exception 발생"))

        // when-then
        Assertions.assertThrows(CashApiCallException::class.java) {
            cashConsumerService.consumeChargingProcess(message = eventId.toString())
        }
    }

    @Test
    fun `Cash Consumer - 결제 API 를 호출하기 전 충전트랜잭션을 조회할 때 Completed 이력이 있다면 InvalidChargingTransactionException 발생`() {
        // given
        val cashConsumerService =
            CashConsumerServiceImpl(cashClient = cashFeignClient, paymentCallService = paymentCallService)
        `when`(cashFeignClient.getTransactionList(transactionId))
            .thenReturn(ResponseEntity.ok(this.getMockCompletedTransactionDataList()))

        // when-then
        Assertions.assertThrows(InvalidChargingTransactionException::class.java) {
            cashConsumerService.getRequestTransactionData(transactionId = transactionId.toString())
        }
    }

    @Test
    fun `Cash Consumer - 충전 결제 API 호출 응답 실패 결과로 RETRY_REQUIRED 값을 받으면 RetryPaymentCallRequiredException 발생`() {
        // given
        val cashConsumerService =
            CashConsumerServiceImpl(cashClient = cashFeignClient, paymentCallService = paymentCallService)
        `when`(cashFeignClient.getTransactionList(eventId)).thenReturn(ResponseEntity.ok(this.getMockTransactionDataList()))
        `when`(
            paymentCallService.call(
                accountId = this.getMockTransactionDataList()[0].accountId,
                amount = this.getMockTransactionDataList()[0].amount
            )
        )
            .thenReturn(
                PaymentApiResponse(
                    code = PaymentErrorCodeConstants.RETRY_REQUIRED,
                    desc = PaymentErrorCodeConstants.RETRY_REQUIRED.toString()
                )
            )

        // then
        Assertions.assertThrows(RetryPaymentCallRequiredException::class.java) {
            cashConsumerService.consumeChargingProcess(message = eventId.toString())
        }
    }

    @Test
    fun `Cash Consumer - 충전 결제 API 호출 응답 실패 결과로 그 외 예외코드를 받으면 ChargePayException 발생`() {
        // given
        val cashConsumerService =
            CashConsumerServiceImpl(cashClient = cashFeignClient, paymentCallService = paymentCallService)
        `when`(cashFeignClient.getTransactionList(eventId)).thenReturn(ResponseEntity.ok(this.getMockTransactionDataList()))
        `when`(
            paymentCallService.call(
                accountId = this.getMockTransactionDataList()[0].accountId,
                amount = this.getMockTransactionDataList()[0].amount
            )
        )
            .thenReturn(
                PaymentApiResponse(
                    code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    desc = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase
                )
            )

        // then
        Assertions.assertThrows(ChargePayException::class.java) {
            cashConsumerService.consumeChargingProcess(message = eventId.toString())
        }
    }

    private fun getMockTransactionDataList(): List<CashTransactionResponseView> =
        arrayListOf(
            CashTransactionResponseView(
                id = eventId,
                transactionId = ObjectId(),
                amount = 100000,
                userId = 28L,
                status = CashTransactionStatusType.REQUEST,
                accountId = 1L,
                eventType = CashEventType.CHARGING
            )
        )

    private fun getMockCompletedTransactionDataList(): List<CashTransactionResponseView> =
        arrayListOf(
            CashTransactionResponseView(
                id = ObjectId(),
                transactionId = transactionId,
                amount = 100000,
                userId = 28L,
                status = CashTransactionStatusType.REQUEST,
                accountId = 1L,
                eventType = CashEventType.CHARGING
            ),
            CashTransactionResponseView(
                id = ObjectId(),
                transactionId = transactionId,
                amount = 100000,
                userId = 28L,
                status = CashTransactionStatusType.COMPLETED,
                accountId = 1L,
                eventType = CashEventType.CHARGING
            )
        )

}

