package com.racket.cash.entity

import com.racket.cash.enums.ChargingWayType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id


// 유저가 저장해놓은 충전 계좌
@Entity
class UserChargingWayInfo(

    @Id @GeneratedValue
    @Column(name = "id", nullable = false)
    val id: Long,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "seq", nullable = false)
    val seq: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "charging_way", nullable = false)
    val chargingWayType: ChargingWayType,

    @Column(name = "bank_id", nullable = false)
    val bankId: Long,

    @Column(name = "account", nullable = false)
    val account: String,

    @Column(name = "useYn", nullable = false)
    var useYn: Boolean
) {

    fun updateUseYn(isUse: Boolean) {
        this.useYn = isUse
    }

    fun isInvalidChargingWay() = !this.useYn

}