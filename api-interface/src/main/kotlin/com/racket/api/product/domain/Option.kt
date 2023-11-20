package com.racket.api.product.domain

import com.racket.api.product.domain.enums.ProductStatusType
import javax.persistence.*

@Entity
@Table(name = "product_option")
class Option(

    @Id
    @Column(name = "option_id")
    var id: String,

    @Column(name = "product_id")
    var productId: String,   // 상품 ID

    var name: String,   // 옵션명

    var sort: Int,  // 옵션 정렬

    var price: Long,  // 옵션 가격

    var stock: Int, // 재고 수량

    var status: ProductStatusType = ProductStatusType.INITIATED, // 판매 상태

    var description: String? = null, // 비고

    @Column(name = "display_yn")
    var displayYn: Boolean
) {

    fun isAvailable() = stock > 0

    fun updateInfo(
        productId: String,
        name: String,
        sort: Int,
        price: Long,
        stock: Int,
        status: ProductStatusType,
        description: String?,
        displayYn: Boolean
    ): Option {
        this.productId = productId
        this.name = name
        this.sort = sort
        this.price = price
        this.stock = stock
        this.status = status
        this.description = description
        this.displayYn = displayYn
        return this
    }

    fun updateDisplayYn(displayYn: Boolean): Option {
        this.displayYn = displayYn
        return this
    }

}