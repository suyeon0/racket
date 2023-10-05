package com.racket.delivery.adapter.out.client.feign.cj

data class CjDeliveryApiRequest (

    val company: String = "CJ",
    val invoiceNo: String,

)