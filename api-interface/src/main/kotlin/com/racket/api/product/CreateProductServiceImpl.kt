package com.racket.api.product

import com.racket.api.admin.product.exception.DuplicateProductException
import com.racket.api.product.domain.Product
import com.racket.api.product.domain.ProductRepository
import com.racket.api.product.presentation.response.ProductResponseView
import com.racket.api.product.vo.ProductRedisHashVO
import mu.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class CreateProductServiceImpl(
    private val productRepository: ProductRepository,
    private val eventPublisher: ApplicationEventPublisher
) : CreateProductService {

    private val log = KotlinLogging.logger { }

    /**
     * 상품 등록
     */
    @Transactional
    override fun registerProduct(productRegisterDTO: CreateProductService.ProductRegisterDTO): ProductResponseView {
        val customerProductCode = productRegisterDTO.customerProductCode
        if (this.isExistDuplicatedCustomerProductCode(customerProductCode)) {
            throw DuplicateProductException(customerProductCode)
        }

        val product = this.productRepository.save(this.createProductEntity(productRegisterDTO))
        // event
        this.eventPublisher.publishEvent(ProductRedisHashVO.of(product))

        return this.makeProductResponseViewFromProduct(product)
    }

    private fun isExistDuplicatedCustomerProductCode(customerProductCode: String) =
        this.productRepository.findByCustomerProductCode(customerProductCode).isPresent

    private fun createProductEntity(productRegisterDTO: CreateProductService.ProductRegisterDTO) =
        Product(
            customerProductCode = productRegisterDTO.customerProductCode,
            name = productRegisterDTO.name,
            price = productRegisterDTO.price
        )

}
