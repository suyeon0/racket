package com.racket.api.product.domain

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductImageRepository: CrudRepository<ProductImage, Long> {
    fun findByProductIdOrderByIdAsc(productId: String): List<ProductImage>
    fun deleteByProductId(productId: String)
}