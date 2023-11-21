package com.racket.cart.api.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "cart")
class Cart(

    @Id @GeneratedValue
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "product_id")
    val productId: String,

    @Column(name = "option_id")
    val optionId: String,

    @Column(name = "product_name")
    val productName: String,

    @Column(name = "option_name")
    val optionName: String,

    @Column(name = "product_representative_image")
    val productRepresentativeImage: String,

    @Column(name = "original_price")
    val originalPrice: Long,

    @Column(name = "calculated_price")
    val calculatedPrice: Long,

    @Column(name = "order_quantity")
    var orderQuantity: Long,

    @Column(name = "delivery_cost")
    val deliveryCost: Long
) {

    fun updateOrderQuantity(quantity: Long) {
        this.orderQuantity = quantity
    }
}