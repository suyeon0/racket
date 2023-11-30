package com.racket.api.product.catalog

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.racket.api.product.GetProductService
import com.racket.api.product.catalog.response.ProductCatalogResponseView
import com.racket.api.product.domain.ProductCatalog
import com.racket.api.product.domain.ProductCatalogRepository
import com.racket.api.product.exception.NotFoundProductCatalogException
import com.racket.api.product.vo.ProductCatalogContents
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductCatalogServiceImpl(
    private val productService: GetProductService,
    private val productCatalogRepository: ProductCatalogRepository
) : ProductCatalogService {

    private val objectMapper = jacksonObjectMapper()

    @Transactional
    override fun addProductCatalog(productId: String, contents: ProductCatalogContents): ProductCatalogResponseView {
        this.productService.getProductResponseView(productId)

        val contentsJson: String = this.objectMapper.writeValueAsString(contents)
        val catalog = productCatalogRepository.save(
            ProductCatalog(
                productId = productId,
                contents = contentsJson
            )
        )

        return ProductCatalogResponseView(
            id = catalog.id!!,
            productId = catalog.productId,
            contents = objectMapper.readValue(contentsJson, ProductCatalogContents::class.java)
        )
    }

    @Transactional(readOnly = true)
    override fun getCatalogByProductId(productId: String): ProductCatalogResponseView {
        val catalog = this.productCatalogRepository.findByProductId(productId)
            .orElseThrow { NotFoundProductCatalogException("Product catalog not found for productId: $productId") }

        return ProductCatalogResponseView(
            id = catalog.id!!,
            productId = catalog.productId,
            contents = this.objectMapper.readValue(catalog.contents, ProductCatalogContents::class.java)
        )
    }
}