package com.racket.api.admin.product.request

import com.racket.api.product.domain.enums.ProductStatusType

data class OptionCreateRequestCommand(

    val productId: String?,

    val name: String?,

    val sort: Int?,

    val price: Long?,

    val stock: Int?,

    val status: ProductStatusType? = ProductStatusType.INITIATED,

    val description: String? = null,

    val displayYn: Boolean?
) {
    fun validate() {
        if (this.productId.isNullOrEmpty()) {
            throw IllegalArgumentException("productId cannot be empty")
        }
        if (this.name.isNullOrEmpty()) {
            throw IllegalArgumentException("name cannot be empty")
        }
        if (this.price == null || this.price < 0) {
            throw IllegalArgumentException("Option Price must be greater than or equal to zero")
        }
        if (this.stock == null || this.stock < 0) {
            throw IllegalArgumentException("stock must be greater than or equal to zero")
        }
        if (this.displayYn == null) {
            throw IllegalArgumentException("displayYn cannot be null")
        }
    }

}