package com.racket.api.payment.presentation

import com.racket.api.payment.presentation.response.PaymentApiResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/payment")
class PaymentController: PaymentSpecification {

    private val log = KotlinLogging.logger { }

    @PostMapping
    override fun pay(request: Long): PaymentApiResponse {
        // 결제하는 것처럼 임의 시간 줌(최대 5초)
        Thread.sleep(this.getRandomNumber())
        //return PaymentApiResponse(code = PaymentErrorCodeConstants.RETRY_REQUIRED, desc = "RETRY 오류 발생")
        return PaymentApiResponse(code = HttpStatus.OK.value(), desc = "success")
    }

    private fun getRandomNumber(): Long {
        val min = 0
        val max = 5000
        return (Math.random() * (max - min) + min).toLong()
    }
}