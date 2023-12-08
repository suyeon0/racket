package com.racket.cash.consume.exception

import com.racket.cash.consume.const.DeadLetterType
import com.racket.cash.consume.vo.DeadLetterQueueVO
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.listener.ErrorHandler
import java.lang.Exception

class ConsumeExceptionHandler: ErrorHandler {

    private val log = KotlinLogging.logger { }

    override fun handle(thrownException: Exception, data: ConsumerRecord<*, *>?) {
        val topic = DeadLetterType.INSERT_DB
        val value =  DeadLetterQueueVO(
            topic = data!!.topic(),
            key = data.key().toString(),
            payload = data.value(),
            errorMessage = thrownException.message
        )
        log.debug("ConsumeExceptionHandler test ::::: ${topic} ${value}")
    }
}