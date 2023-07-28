package com.racket.api.product.domain

import com.racket.api.product.enums.ProductStatusType
import javax.persistence.*

@Entity
@Table(name = "product")
class Product(

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    var id: Long? = null,

    val name: String,

    val price: Long,

    @Enumerated(EnumType.STRING)
    var status: ProductStatusType = ProductStatusType.ACTIVE
) {

    /* 상태 */
    fun isDeletedStatus() = this.status == ProductStatusType.INACTIVE

    fun updateStatus(status: ProductStatusType): Product {
        this.status = status
        return this
    }
}