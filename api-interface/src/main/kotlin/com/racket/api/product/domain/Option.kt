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
    val productId: String,   // 상품 ID

    val name: String,   // 옵션명

    val sort: Int,  // 옵션 정렬

    val price: Long,  // 옵션 가격

    val stock: Int, // 재고 수량

    val status: ProductStatusType = ProductStatusType.INITIATED, // 판매 상태

    val description: String? = null, // 비고

    @Column(name = "display_yn")
    val displayYn: Boolean
) {

    fun isAvailable() = stock > 0

}