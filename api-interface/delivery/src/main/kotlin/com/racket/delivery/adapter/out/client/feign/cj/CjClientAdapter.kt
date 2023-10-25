package com.racket.delivery.adapter.out.client.feign.cj

import com.racket.delivery.adapter.`in`.rest.response.TrackingDeliveryResponseView
import com.racket.delivery.adapter.out.kafka.DeliveryApiLogPayloadVO
import com.racket.delivery.adapter.out.kafka.DeliveryApiLogProducer
import com.racket.delivery.application.port.out.client.DeliveryRequestClientPort
import com.racket.delivery.common.enums.DeliveryCompanyType
import com.racket.delivery.common.exception.TrackingClientFailException
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Component
class CjClientAdapter(
    @Qualifier("cjFakeFeignClient") private val client: CJDeliveryApiFeignClient,
    private val deliveryApiLogProducer: DeliveryApiLogProducer
) : DeliveryRequestClientPort {

    private val log = KotlinLogging.logger { }

    @Transactional
    override fun call(invoiceNo: String): TrackingDeliveryResponseView {
        val response = this.client.getTrackingDeliveryList(request = CjDeliveryApiRequest(invoiceNo = invoiceNo))
            ?: throw TrackingClientFailException()

        return when (response.statusCode) {
            HttpStatus.OK -> {
                val result = response.body as CjDeliveryApiResponse
                callLogProducer(result)
                result.toCommonView()
            }
            else -> throw TrackingClientFailException()
        }
    }

    // 로그 쌓는건 실패해도 상관 없다고 생각
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun callLogProducer(response: CjDeliveryApiResponse) {
        try {
            val payload = DeliveryApiLogPayloadVO(
                companyType = DeliveryCompanyType.CJ,
                invoiceNo = response.invoice,
                responseTime = Instant.now(),
                response = response.toString()
            )
            this.deliveryApiLogProducer.produce(payload)
        } catch (e: Exception) {
            // TODO : delivery log exception
            log.error { e }
        }
    }
}