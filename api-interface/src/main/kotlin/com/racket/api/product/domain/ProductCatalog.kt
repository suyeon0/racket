package com.racket.api.product.domain

import javax.persistence.*

@Entity
@Table(name = "product_catalog")
class ProductCatalog (
    @Id
    @GeneratedValue
    val id: Long? = null,

    @Column(name = "product_id", nullable = false)
    val productId: String,

    @Column(name = "contents", nullable = false, columnDefinition = "JSON")
    val contents: String
)