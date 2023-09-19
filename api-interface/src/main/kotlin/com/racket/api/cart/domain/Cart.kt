package com.racket.api.cart.domain

import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "cart")
class Cart (

    @Id @GeneratedValue
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "product_id")
    val productId: Long,

    @Column(name = "option_id")
    val optionId: Long,

    @Column(name = "origin_price")
    val originPrice: Long,

    @Column(name = "calculated_price")
    val calculatedPrice: Long,

    @Column(name = "order_quantity")
    var orderQuantity: Long,

    @Column(name = "delivery_cost")
    val deliveryCost: Long,

    @Column(name = "estimated_delivery_date")
    val estimatedDeliveryDate: LocalDate

) {
    fun updateOrderQuantity(quantity: Long) {
        this.orderQuantity = quantity
    }
}