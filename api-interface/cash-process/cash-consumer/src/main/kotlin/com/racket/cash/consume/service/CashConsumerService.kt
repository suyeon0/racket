package com.racket.cash.consume.service

import com.racket.cash.consume.client.CashFeignClient
import com.racket.cash.consume.client.PaymentFeignClient
import com.racket.cash.exception.ChargePayException
import com.racket.cash.exception.UpdateBalanceException
import com.racket.cash.presentation.request.UpdateBalanceCommand
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CashConsumerService(

    private val paymentClient: PaymentFeignClient,
    private val cashClient: CashFeignClient
) {

    private val log = KotlinLogging.logger { }

    @KafkaListener(
        topics = ["charging"], groupId = "racket"
    )
    @Transactional
    fun consumeChargingProcess(message: String) {

        // 0. 충전 트랜잭션 상태 확인
        log.info { "=============================== Charging Process Consume Start! ===============================" }

        // 1. 임시로 저장해놓은 트랜잭션 정보 DB 에서 GET
        log.info { "=============================== getTransaction()==============================" }
        val result = this.cashClient.getTransaction(transactionId = message)
        if (result.body == null) {
            throw ChargePayException("임시 트랜잭션 데이터를 가져오지 못했습니다-${message}")
        }
        val chargeAmount: Long = result.body!!.amount
        val userId: Long = result.body!!.userId
        log.info { "=============================== Get 결과 : userId-${userId}, ${chargeAmount}원 요청 ===========================" }

        // 2. 결제 API 호출
        this.payProcess(chargeAmount)

        // 3. 충전 합계 테이블 반영
        this.updateBalance(userId = userId, amount = chargeAmount)

        log.info { "=============================== Charging Process Consume Done! ===============================" }

    }

    private fun updateBalance(userId: Long, amount: Long) {
        log.info { "===============================충전 합계 테이블 반영 시작==============================" }
        try {
            val result = this.cashClient.postToUpdateBalance(
                UpdateBalanceCommand(
                    userId = userId, amount = amount
                )
            )
            val balance = result.body!!.balance
            log.info {
                "===============================충전 합계 테이블 반영 완료==============================" +
                "=============================== 잔액: ${balance} 원 =============================="
            }
        } catch (e: Exception) {
            log.error { e.printStackTrace() }
            throw UpdateBalanceException()
        }
    }


    // 결제모듈 연동
    private fun payProcess(price: Long) {
        log.info { "===============================충전 결제 모듈 연동 시작==============================" }
        val paymentResultMessage: String = this.paymentClient.pay(price)
        if ("COMPLETE".equals(paymentResultMessage, ignoreCase = true)) {
            log.info { "===============================충전 결제 성공으로 완료===============================" }
            //savedOrderProduct.setStatus(OrderStatus.COMPLETE)
        } else {
            //savedOrderProduct.setStatus(OrderStatus.FAILED)
            throw ChargePayException("===============================결제 모듈 연동 실패!===============================")
        }
    }
}