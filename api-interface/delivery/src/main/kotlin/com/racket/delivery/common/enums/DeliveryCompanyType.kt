package com.racket.delivery.common.enums

enum class DeliveryCompanyType {
    CJ,
    HANJIN;

    companion object {
        fun isInValidDeliveryCompany(deliveryCompanyType: DeliveryCompanyType) = deliveryCompanyType !in values()
    }
}