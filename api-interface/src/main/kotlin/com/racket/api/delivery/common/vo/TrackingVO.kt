package com.racket.api.delivery.common.vo

import com.racket.delivery.common.enums.DeliveryStatusType
import java.time.Instant

data class TrackingVO (

    private val timestamp: Instant, // 처리일시
    private val currentLocation: String, // 현재위치
    private val driver: String,
    private val deliveryStatus: DeliveryStatusType // 배송상태

)