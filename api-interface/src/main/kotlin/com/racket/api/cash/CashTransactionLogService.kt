package com.racket.api.cash

import com.racket.api.cash.response.CashTransactionResponseView
import com.racket.api.cash.vo.ChargeVO
import com.racket.share.domain.cash.entity.CashTransaction
import org.bson.types.ObjectId

interface CashTransactionLogService {

    fun getTransactionListByTransactionId(transactionId: String): List<CashTransactionResponseView>

    fun insertChargeTransaction(chargeVO: ChargeVO): CashTransaction

    fun getTransactionById(eventId: ObjectId): CashTransactionResponseView

}