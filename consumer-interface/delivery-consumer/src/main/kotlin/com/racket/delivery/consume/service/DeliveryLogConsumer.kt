package com.racket.delivery.consume.service

import com.racket.delivery.consume.domain.DeliveryApiLog
import com.racket.delivery.consume.mongo.DeliveryApiLogRepositoryAdapter
import com.racket.delivery.consume.vo.DeliveryApiLogPayloadVO
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.Consumer
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class DeliveryLogConsumer(
    private val deliveryApiLogRepositoryAdapter: DeliveryApiLogRepositoryAdapter
) {

    private val log = KotlinLogging.logger { }

    @KafkaListener(
        topics = ["delivery-log"], groupId = "racket"
    )
    fun consumeSaveLog(@Payload message: DeliveryApiLogPayloadVO, consumer: Consumer<String, DeliveryApiLogPayloadVO>) {
        try {
            val log = DeliveryApiLog(
                companyType = message.companyType,
                invoiceNo = message.invoiceNo,
                responseTime = LocalDateTime.ofInstant(message.responseTime, ZoneId.systemDefault()),
                response = message.response
            )
            this.deliveryApiLogRepositoryAdapter.save(log)
        } catch (e: Exception) {
            log.error { e.message }
        }
    }


}