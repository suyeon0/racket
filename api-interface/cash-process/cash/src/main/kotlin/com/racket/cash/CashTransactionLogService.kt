package com.racket.cash

import com.racket.cash.entity.CashTransaction
import com.racket.cash.enums.CashTransactionStatusType
import com.racket.cash.response.CashTransactionResponseView
import com.racket.cash.vo.ChargeVO
import org.bson.types.ObjectId

interface CashTransactionLogService {

    fun getTransactionById(transactionId: ObjectId): CashTransactionResponseView

    fun insertChargeTransaction(chargeVO: ChargeVO): CashTransaction

}