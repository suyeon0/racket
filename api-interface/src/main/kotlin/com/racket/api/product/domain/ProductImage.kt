package com.racket.api.product.domain

import javax.persistence.*

@Entity
@Table(name = "product_image")
data class ProductImage(
    @Id
    @GeneratedValue
    val id: Long? = null,

    @Column(name = "product_id", nullable = false)
    val productId: String,

    @Column(name = "image_url", nullable = false)
    val imageUrl: String,

)