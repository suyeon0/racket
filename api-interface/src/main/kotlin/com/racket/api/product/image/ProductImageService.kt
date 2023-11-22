package com.racket.api.product.image

import com.racket.api.product.image.request.ProductImageCreateRequestCommand
import com.racket.api.product.image.response.ProductImageResponseView

interface ProductImageService {

    fun getImageListByProductId(productId: String): List<ProductImageResponseView>
    fun addProductImages(request: ProductImageCreateRequestCommand): List<ProductImageResponseView>
}