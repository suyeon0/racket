package com.racket.api.product.domain

import com.racket.api.product.domain.enums.ProductStatusType
import javax.persistence.*

@Entity
@Table(name = "product")
class Product(

    @Id
    @Column(name = "product_id")
    var id: String,

    var name: String,

    var price: Long,

    @Enumerated(EnumType.STRING)
    var status: ProductStatusType = ProductStatusType.ACTIVE
) {

    /* 상태 */
    fun isDeletedStatus() = this.status == ProductStatusType.INACTIVE

    fun updateStatus(status: ProductStatusType): Product {
        this.status = status
        return this
    }

    fun updateProductInfo(name: String, price: Long): Product {
        this.name = name
        this.price = price
        return this
    }
}