package com.racket.cash.publish

import mu.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Service
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.util.concurrent.ListenableFutureCallback

@Service
class KafkaPublishServiceImpl(
    val kafkaTemplate: KafkaTemplate<String, String>
) : PublishService {

    private val log = KotlinLogging.logger { }

    override fun send(topic: String, message: String) {
        log.info("Produce Message - BEGIN")
        val listenableFuture: ListenableFuture<SendResult<String, String>> = kafkaTemplate.send(topic, message)
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