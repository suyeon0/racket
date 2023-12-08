package com.racket.delivery.adapter.out.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.util.concurrent.ListenableFutureCallback

@Component
class DeliveryApiLogProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {

    private val topic: String = "delivery-log"
    private val log = KotlinLogging.logger { }

    fun produce(@Payload payload: DeliveryApiLogPayloadVO) {
        val key = payload.invoiceNo
        val payloadString = this.objectMapper.writeValueAsString(payload)
        val listenableFuture: ListenableFuture<SendResult<String, String>> = kafkaTemplate.send(topic, key, payloadString)
        listenableFuture.addCallback(object : ListenableFutureCallback<SendResult<String, String>> {
            override fun onFailure(e: Throwable) {
                log.error { "ERROR Kafka error happened-${e}" }
            }

            override fun onSuccess(result: SendResult<String, String>?) {
                log.info {
                    "Produce Send Result : SUCCESS! " +
                        " Message :: ${result}"
                }
            }
        })
    }
}

