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

    @OneToMany(mappedBy = "product")
    var options: List<Option> = ArrayList(),

    @Enumerated(EnumType.STRING)
    var status: ProductStatusType = ProductStatusType.ACTIVE
) {

    /* 판매가 */
    fun applyOptionAndGetSalePrice(optionId: Long): Long {
        var salePrice = price
        val option = options.find { it.id == optionId }

        if (option != null) {
            salePrice += option.optionAdditionalPrice // 기본 판매가에 옵션 가격 반영
        }
        return salePrice
    }

    /* 상태 */
    fun isDeletedStatus() = this.status == ProductStatusType.INACTIVE

    fun updateStatus(status: ProductStatusType): Product {
        this.status = status
        return this
    }
}