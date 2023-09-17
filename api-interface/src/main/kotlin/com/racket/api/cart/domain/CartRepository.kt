package com.racket.api.cart.domain

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CartRepository: CrudRepository<Cart, Long> {
    fun findAllByUserId(userId: Long): List<Cart>
}