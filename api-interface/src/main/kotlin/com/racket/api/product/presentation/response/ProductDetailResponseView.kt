package com.racket.api.product.presentation.response

import com.racket.api.product.catalog.response.ProductCatalogResponseView
import com.racket.api.product.domain.enums.ProductStatusType
import com.racket.api.product.image.response.ProductImageResponseView
import com.racket.api.product.option.response.OptionResponseView

data class ProductDetailResponseView (
    val id: String?,
    val name: String?,
    val price: Long?,
    val statusType: ProductStatusType?,

    // 옵션
    val options: List<OptionResponseView>?,

    // 이미지
    val images: List<ProductImageResponseView>?,

    // 카탈로그
    val catalog: ProductCatalogResponseView?
)
