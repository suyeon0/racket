package com.racket.api.product.component

import com.racket.api.product.domain.Product
import com.racket.api.product.response.ProductResponseView
import org.springframework.stereotype.Component

@Component
class BaseProductComponent {

    fun makeProductResponseViewFromProduct(product: Product) =
        ProductResponseView(
            id = product.id,
            name = product.name,
            price = product.price,
            statusType = product.status
        )

}