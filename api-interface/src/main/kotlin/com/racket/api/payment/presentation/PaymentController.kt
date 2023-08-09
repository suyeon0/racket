package com.racket.api.payment.presentation

import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/payment")
class PaymentController: PaymentSpecification {

    private val log = KotlinLogging.logger { }

    @PostMapping
    override fun pay(request: Long): ResponseEntity<String> {
        // 결제하는 것처럼 임의 시간 줌(최대 5초)
        try {
            Thread.sleep(this.getRandomNumber())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        log.info { "결제 성공! 결제한 금액 :${request} 원" }
        return ResponseEntity.ok("COMPLETE")
    }

    private fun getRandomNumber(): Long {
        val min = 0
        val max = 5000
        return (Math.random() * (max - min) + min).toLong()
    }
}