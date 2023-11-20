package com.racket.api.product.domain

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OptionRepository: CrudRepository<Option, String> {
    fun findByProductId(productId: String): List<Option>
}