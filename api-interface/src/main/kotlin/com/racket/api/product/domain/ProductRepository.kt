package com.racket.api.product.domain

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository: JpaRepository<Product, Long> {
    fun findAllByOrderByIdDesc(page: Pageable): List<Product>
    fun findByIdLessThanOrderByIdDesc(id: Long, page: Pageable): List<Product>
    fun existsByIdLessThan(id: Long): Boolean
}