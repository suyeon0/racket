package com.racket.api.product

import com.racket.api.product.domain.ProductRepository
import com.racket.api.product.enums.ProductStatusType
import com.racket.api.product.exception.NotFoundProductException
import com.racket.api.product.response.ProductResponseView
import org.springframework.stereotype.Service

@Service
class UpdateProductServiceImpl(
    val productRepository: ProductRepository
) : UpdateProductService {
    override fun updateProductInfo(id: Long, name: String, price: Long): ProductResponseView =
        this.makeProductResponseViewFromProduct(
            this.getProductEntity(id)
                .updateProductInfo(
                    name = name,
                    price = price
                )
        )

    override fun updateProductStatus(id: Long, status: ProductStatusType): ProductResponseView =
        this.makeProductResponseViewFromProduct(
            this.getProductEntity(id)
                .updateStatus(status = status)
        )

    private fun getProductEntity(id: Long) =
        this.productRepository.findById(id).orElseThrow { NotFoundProductException() }
}