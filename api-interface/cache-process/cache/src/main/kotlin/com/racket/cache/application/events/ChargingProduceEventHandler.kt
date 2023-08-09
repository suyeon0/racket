package com.racket.cache.application.events

import com.racket.api.shared.request.MessageRequest
import com.racket.cache.infrastructure.client.ProducerApiClient
import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ChargingProduceEventHandler(
    private val chargingProduceEvent: ChargingProduceEvent
) {

    @Async("chargingEventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handle(chargingProduceEventVO: ChargingProduceEventVO) {
        this.chargingProduceEvent.callProduce(chargingProduceEventVO.eventId)
    }

}

@Component
class ChargingProduceEvent(
    private val producerApiClient: ProducerApiClient
) {

    private val log = KotlinLogging.logger { }

    fun callProduce(chargingEventId: ObjectId) {
        this.producerApiClient.sendMessage(
            MessageRequest(
                topic = "charging",
                message = chargingEventId.toString()
            )
        )
        log.info { "call Produce done! -${chargingEventId}" }
    }
}

data class ChargingProduceEventVO(
    val eventId: ObjectId
)