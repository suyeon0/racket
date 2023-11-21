package com.racket.api.product.presentation.response

import com.racket.api.product.domain.enums.ProductStatusType
import com.racket.api.product.option.response.OptionResponseView
import io.swagger.v3.oas.annotations.media.Schema

data class OptionWithProductView(

    @Schema(title = "옵션 ID", example = "1")
    val id: String,
    @Schema(title = "상품 ID", example = "10")
    val productId: String,
    @Schema(title = "옵션명", example = "V14 골드 315g")
    val name: String,
    @Schema(title = "옵션금액", example = "10000")
    val price: Long,
    @Schema(title = "재고수량합계", example = "50")
    val stock: Int,
    @Schema(title = "부가설명", example = "부가설명입니다")
    val description: String?,
    @Schema(title = "정렬순서", example = "0", minimum = "0")
    val sort: Int,
    @Schema(title = "사용여부", example = "true")
    val displayYn: Boolean,

    @Schema(title = "상품명", example = "윌슨 라켓")
    val productName: String,
    @Schema(title = "상품금액", example = "15000")
    val productPrice: Long,
    @Schema(title = "총금액", example = "25000")
    val totalPrice: Long,
    @Schema(title = "상품상태")
    val statusType: ProductStatusType

) {
    companion object {
        fun of(product: ProductResponseView, option: OptionResponseView): OptionWithProductView =
            OptionWithProductView(
                id = option.id,
                productId = product.id,
                name = option.name,
                price = option.price,
                stock = option.stock,
                description = option.description,
                sort = option.sort,
                displayYn = option.displayYn,
                productName = product.name,
                productPrice = product.price,
                totalPrice = product.price + option.price,
                statusType = product.statusType
            )
    }
}