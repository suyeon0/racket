package com.racket.consumer.service.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.racket.api.delivery.adapter.out.kafka.DeliveryApiLogPayloadVO
import com.racket.consumer.domain.delivery.DeliveryApiLog
import com.racket.consumer.domain.delivery.DeliveryApiLogRepositoryAdapter
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.Consumer
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class DeliveryEventConsumer(
    private val deliveryApiLogRepositoryAdapter: DeliveryApiLogRepositoryAdapter,
    private val objectMapper: ObjectMapper
) {

    private val log = KotlinLogging.logger { }

    @KafkaListener(
        topics = ["delivery-log"],
        groupId = "racket"
    )
    @Transactional
    fun consumeSaveLog(
        @Payload payload: String,
        consumer: Consumer<String, String>
    ) {
        try {
            val payload = this.objectMapper.readValue(payload, DeliveryApiLogPayloadVO::class.java)
            val deliveryApiLog = DeliveryApiLog(
                companyType = payload.companyType,
                invoiceNo = payload.invoiceNo,
                responseTime = LocalDateTime.ofInstant(payload.responseTime, ZoneId.systemDefault()),
                response = payload.response
            )
            this.deliveryApiLogRepositoryAdapter.save(deliveryApiLog)
            consumer.commitSync()

            log.info { "cash consume offset commit!" }
        } catch (e: Exception) {
            log.error { "Consumer Failed. ${e.message}" }
        }
    }


}