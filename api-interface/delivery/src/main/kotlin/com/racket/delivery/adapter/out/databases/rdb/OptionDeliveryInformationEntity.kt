package com.racket.delivery.adapter.out.databases.rdb

import com.racket.delivery.domain.OptionDeliveryInformation
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "option_delivery_information")
class OptionDeliveryInformationEntity(

    @Id @GeneratedValue
    private val id: Long = 0L,

    private val optionId: Long,

    private val days: Long,

    private val cost: Long

) {
    fun toModel() =
        OptionDeliveryInformation(
            id = id,
            optionId = optionId,
            deliveryDays = days,
            deliveryCost = cost
        )

    companion object {
        fun from(optionDeliveryInformation: OptionDeliveryInformation) =
            OptionDeliveryInformationEntity(
                id = optionDeliveryInformation.id,
                optionId = optionDeliveryInformation.optionId,
                days = optionDeliveryInformation.deliveryDays,
                cost = optionDeliveryInformation.deliveryCost
            )
    }
}