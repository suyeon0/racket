package com.racket.cash.consume.service.consume

import com.racket.cash.response.CashTransactionResponseView

interface CashConsumerService {

    fun consumeChargingProcess(message: String)

    fun getRequestTransactionData(transactionId: String): CashTransactionResponseView

}