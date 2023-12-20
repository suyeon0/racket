package com.racket.consumer.handler

import com.racket.consumer.enums.DeadLetterType
import com.racket.consumer.vo.DeadLetterQueueVO
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.ErrorHandler
import org.springframework.kafka.support.SendResult
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.util.concurrent.ListenableFutureCallback
import java.lang.Exception

class ConsumeExceptionHandler(
    private val deadLetterQueueKafkaProduceTemplate: KafkaTemplate<String, DeadLetterQueueVO>
): ErrorHandler {

    private val log = KotlinLogging.logger { }

    // 재처리 따로 지정하지 않을 때 ErrorHandler 로 와서 무조건 실패 DB 로 넣도록
    override fun handle(thrownException: Exception, data: ConsumerRecord<*, *>?) {

//        val value =  DeadLetterQueueVO(
//            deadLetterType = DeadLetterType.INSERT_DB,
//            failureTopic = data!!.topic(),
//            key = data.key().toString(),
//            payload = data.value(),
//            errorMessage = thrownException.message
//        )
//        this.publishDeadLetterQueue(value)
    }

    private fun publishDeadLetterQueue(value: DeadLetterQueueVO) {
        val listenableFuture: ListenableFuture<SendResult<String, DeadLetterQueueVO>> =
            this.deadLetterQueueKafkaProduceTemplate.send("dlq", value)
        listenableFuture.addCallback(object : ListenableFutureCallback<SendResult<String, DeadLetterQueueVO>> {
            override fun onFailure(e: Throwable) {
                log.error { "publish DLQ Send Failed!::${e}" }
            }

            override fun onSuccess(result: SendResult<String, DeadLetterQueueVO>?) {
                log.info {
                    "publish DLQ Send SUCCESS!::${result}"
                }
            }
        })
    }

}