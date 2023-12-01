package com.racket.api.product.domain

import javax.persistence.*

@Entity
@Table(name = "product_image")
data class ProductImage(
    @Id
    @Column(name = "image_id")
    val id: String,

    @Column(name = "product_id", nullable = false)
    val productId: String,

    @Column(name = "origin_file_name", nullable = false)
    val originFileName: String
)