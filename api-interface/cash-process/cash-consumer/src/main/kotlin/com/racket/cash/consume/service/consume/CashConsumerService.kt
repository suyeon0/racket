package com.racket.cash.consume.service.consume

import com.racket.cash.entity.CashTransaction
import com.racket.cash.presentation.response.CashTransactionResponseView
import org.bson.types.ObjectId

interface CashConsumerService {

    fun consumeChargingProcess(message: String)

    fun makeCashTransactionResponseToEntity(responseView: CashTransactionResponseView) =
        CashTransaction(
            id = ObjectId(responseView.id),
            transactionId = ObjectId(responseView.transactionId),
            status = responseView.status,
            userId = responseView.userId,
            amount = responseView.amount,
            accountId = responseView.accountId,
            eventType = responseView.eventType
        )

}