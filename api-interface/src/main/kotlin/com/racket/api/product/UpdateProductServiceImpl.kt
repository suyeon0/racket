package com.racket.api.product

import com.racket.api.product.domain.ProductRepository
import com.racket.api.product.domain.enums.ProductStatusType
import com.racket.api.product.exception.NotFoundProductException
import com.racket.api.product.presentation.response.ProductResponseView
import com.racket.api.product.vo.ProductRedisHashVO
import mu.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateProductServiceImpl(
    private val productRepository: ProductRepository,
    private val eventPublisher: ApplicationEventPublisher
) : UpdateProductService {

    private val log = KotlinLogging.logger { }

    @Transactional
    override fun updateProductInfo(id: String, name: String, price: Long): ProductResponseView {
        val product = this.getProductEntity(id).updateProductInfo(name = name, price = price)

        this.productRepository.save(product)
        // event
        this.eventPublisher.publishEvent(ProductRedisHashVO.of(product))

        return this.makeProductResponseViewFromProduct(product)
    }

    override fun updateProductStatus(id: String, status: ProductStatusType): ProductResponseView {
        val entity = this.getProductEntity(id).updateStatus(status = status)
        this.productRepository.save(entity)
        return this.makeProductResponseViewFromProduct(entity)
    }

    private fun getProductEntity(id: String) =
        this.productRepository.findById(id).orElseThrow { NotFoundProductException() }
}