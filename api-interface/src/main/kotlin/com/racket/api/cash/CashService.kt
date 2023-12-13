package com.racket.api.cash

import com.racket.api.cash.response.CashBalanceResponseView
import com.racket.api.cash.response.ChargeResponseView
import com.racket.api.cash.vo.ChargeVO

interface CashService {

    fun requestCharge(chargeVO: ChargeVO): ChargeResponseView

    fun completeCharge(chargeVO: ChargeVO): CashBalanceResponseView

    fun getChargeUnitSet(): Set<Long>

    fun getBalanceByUserId(userId: Long): CashBalanceResponseView

}