package com.racket.api.product.image

import com.racket.api.product.image.response.ProductImageResponseView
import org.springframework.web.multipart.MultipartFile

interface ProductImageService {

    fun getImageListByProductId(productId: String): List<ProductImageResponseView>
    fun addProductImages(productId: String, imageFiles: List<MultipartFile>): List<ProductImageResponseView>
}