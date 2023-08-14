package com.racket.cash.presentation.request

data class CreateChargeCommand(
    val userId: Long,
    val amount: Long,
    val accountId: Long
) {
    fun validate(validChargeUnitSet: Set<Long>) {
        // 충전 단위 체크
        if(!validChargeUnitSet.contains(this.amount)) {
            throw IllegalArgumentException("Invalid Charge Unit!")
        }
    }
}