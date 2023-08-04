package com.racket.api.product.domain

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductRepository: CrudRepository<Product, Long> {
    fun findAllByOrderByIdDesc(page: Pageable): List<Product>

    fun findByIdLessThanOrderByIdDesc(id: Long, page: Pageable): List<Product>

    fun existsByIdLessThan(id: Long): Boolean

    fun findByCustomerProductCode(customerProductCode: String): Optional<Product>
}