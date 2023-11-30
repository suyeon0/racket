package com.racket.api.product

import com.racket.api.product.presentation.response.ProductResponseView
import com.racket.api.product.vo.ProductCursorResultVO
import org.springframework.data.domain.Pageable

interface GetProductService: ProductService {

    fun getProductResponseView(productId: String): ProductResponseView

    fun getList(cursorId: String?, page: Pageable): ProductCursorResultVO

}