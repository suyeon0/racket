package com.racket.api.product

import com.racket.api.shared.vo.CursorResult
import com.racket.api.product.response.ProductResponseView
import org.springframework.data.domain.Pageable

interface ProductService {
    fun getByProductId(productId: Long): ProductResponseView

    fun getList(cursorId: Long?, page: Pageable): CursorResult<ProductResponseView>
}