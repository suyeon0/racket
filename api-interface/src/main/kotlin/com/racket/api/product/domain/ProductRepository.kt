package com.racket.api.product.domain

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository: CrudRepository<Product, String> {
    fun findAllByOrderByIdDesc(page: Pageable): List<Product>

    fun findAllByIdLessThanOrderByIdDesc(id: String, page: Pageable): List<Product>

    fun existsByIdLessThan(id: String): Boolean

}