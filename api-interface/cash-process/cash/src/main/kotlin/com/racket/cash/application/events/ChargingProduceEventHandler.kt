package com.racket.cash.application.events

import com.racket.cash.exception.CashProducerException
import com.racket.cash.infrastructure.kafka.produce.ProduceService
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

    private val log = KotlinLogging.logger { }

    @Async("chargingEventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handle(chargingProduceEventVO: ChargingProduceEventVO) {
        try {
            this.chargingProduceEvent.callProduce(chargingProduceEventVO.eventId)
        } catch (e: Exception) {
            throw CashProducerException(chargingProduceEventVO, e)
        }
    }

}

@Component
class ChargingProduceEvent(
    private val produceService: ProduceService
) {

    private val log = KotlinLogging.logger { }

    fun callProduce(chargingEventId: ObjectId) {
        this.produceService.send(
            topic = "charging",
            message = chargingEventId.toString()
        )
        log.info { "call Produce done! -${chargingEventId}" }
    }
}

data class ChargingProduceEventVO(
    val eventId: ObjectId
)