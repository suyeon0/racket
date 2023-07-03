package com.racket.api.product.domain

import javax.persistence.*

@Entity
@Table(name = "product_option")
class Option(

    @Id
    @GeneratedValue
    @Column(name = "option_id")
    var id: Long? = null,

    val optionNo: String,

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    val product: Product,

    val name: String,

    val additionalPrice: Long,

    val price: Long = product.price + additionalPrice,

    val stock: Int,

    val remark: String = ""

) {

    fun isAvailable() = stock > 0

}