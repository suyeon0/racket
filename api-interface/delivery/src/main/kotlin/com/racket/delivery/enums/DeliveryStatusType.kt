package com.racket.delivery.enums

enum class DeliveryStatusType(code: Int, outputName: String) {
    PICKUP(0, "집하"),
    OUT_FOR_DELIVERY(1, "배송중 (출고)"),
    IN_TRANSIT(2, "배송중 (입고)"),
    ARRIVED_AT_DESTINATION(3, "배달중"),
    ON_DELIVERY(4,"배달지 도착"),
    DELIVERED(5,"배달완료")
}