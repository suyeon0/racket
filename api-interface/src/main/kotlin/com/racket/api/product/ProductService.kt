package com.racket.api.product

import com.racket.api.product.response.ProductResponseView
import com.racket.api.product.vo.ProductCursorResultVO
import org.springframework.data.domain.Pageable

interface ProductService {
    fun getByProductId(productId: Long): ProductResponseView

    fun getList(cursorId: Long?, page: Pageable): ProductCursorResultVO
}