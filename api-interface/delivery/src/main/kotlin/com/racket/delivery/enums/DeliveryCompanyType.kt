package com.racket.delivery.enums

enum class DeliveryCompanyType {
    CJ,
    HANJIN;

    companion object {
        fun isInValidDeliveryCompany(deliveryCompanyType: DeliveryCompanyType) = deliveryCompanyType !in values()
    }
}