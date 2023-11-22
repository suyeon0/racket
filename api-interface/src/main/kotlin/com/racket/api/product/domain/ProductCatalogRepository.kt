package com.racket.api.product.domain

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductCatalogRepository: CrudRepository<ProductCatalog, Long> {

    fun findByProductId(productId: String): Optional<ProductCatalog>
}