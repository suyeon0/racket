package com.racket.cash

import com.racket.cash.response.CashBalanceResponseView
import com.racket.cash.response.CashTransactionResponseView
import com.racket.cash.response.ChargeResponseView
import com.racket.cash.vo.ChargeVO
import org.bson.types.ObjectId

interface CashService {

    fun requestCharge(chargeVO: ChargeVO): ChargeResponseView

    fun completeCharge(chargeVO: ChargeVO): CashBalanceResponseView

    fun getChargeUnitSet(): Set<Long>

    fun getBalanceByUserId(userId: Long): CashBalanceResponseView

}