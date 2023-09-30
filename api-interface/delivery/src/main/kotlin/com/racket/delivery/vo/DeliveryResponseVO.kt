package com.racket.delivery.vo

data class DeliveryResponseVO (

    val invoiceNumber: String,

    val driver: String,

    val timeLine: List<TrackingVO>

)