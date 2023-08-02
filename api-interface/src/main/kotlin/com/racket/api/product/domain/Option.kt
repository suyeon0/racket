package com.racket.api.product.domain

import com.racket.api.product.domain.enums.ProductStatusType
import javax.persistence.*

@Entity
@Table(name = "product_option")
class Option(

    @Id
    @GeneratedValue
    @Column(name = "option_id")
    var id: Long? = null,

    val productId: Long,   // 상품 ID

    val name: String,   // 옵션명

    val optionNo: String,   // 고객사 옵션코드

    val sort: Int,  // 옵션 정렬

    val optionAdditionalPrice: Long,  // 옵션 가격

    val stock: Int, // 재고 수량

    val orderStatus: ProductStatusType = ProductStatusType.INITIATED, // 판매 상태

    val remark: String = "" // 비고

) {

    fun isAvailable() = stock > 0

}