package com.racket.delivery.vo

import com.racket.delivery.enums.DeliveryStatusType
import java.time.Instant

data class TrackingVO (

    val timestamp: Instant, // 처리일시
    val currentLocation: String, // 현재위치
    val deliveryStatus: DeliveryStatusType // 배송상태

)