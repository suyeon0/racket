package com.racket.api.product

import com.racket.api.common.vo.CursorResult
import com.racket.api.product.response.ProductResponseView
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

interface ProductService {
    fun getByProductId(productId: Long): ProductResponseView

    fun getList(cursorId: Long?, page: Pageable): CursorResult<ProductResponseView>
}