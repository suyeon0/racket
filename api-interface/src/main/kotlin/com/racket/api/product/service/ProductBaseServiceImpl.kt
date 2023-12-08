package com.racket.api.product.service

import com.racket.api.product.component.BaseProductComponent
import com.racket.api.product.domain.Product
import com.racket.api.product.domain.ProductRepository
import com.racket.api.product.domain.enums.ProductStatusType
import com.racket.api.product.exception.CacheStoreProcessException
import com.racket.api.product.exception.InvalidProductStatusException
import com.racket.api.product.exception.NotFoundProductException
import com.racket.api.product.response.ProductResponseView
import com.racket.api.product.vo.ProductCursorResultVO
import com.racket.api.product.vo.ProductRedisHashVO
import com.racket.api.product.vo.ProductRegisterVO
import com.racket.api.util.RedisUtils
import com.racket.core.cache.CacheKey
import mu.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ProductBaseServiceImpl(
    private val baseProductComponent: BaseProductComponent,
    private val productRepository: ProductRepository,
    private val redisUtils: RedisUtils,
    private val eventPublisher: ApplicationEventPublisher
) : ProductBaseService {

    private val log = KotlinLogging.logger { }

    @Transactional
    override fun register(productRegisterDTO: ProductRegisterVO): ProductResponseView {
        val product = Product(
            id = productRegisterDTO.id,
            name = productRegisterDTO.name,
            price = productRegisterDTO.price
        )
        val productFromDb = this.productRepository.save(product)

        try {
            // event
            this.eventPublisher.publishEvent(ProductRedisHashVO.of(product))
        } catch (ex: Exception) {
            log.error(ex) {
                "product register event publish error. product id: ${productFromDb.id}, product name: ${productFromDb.name}"
            }
        }

        return this.baseProductComponent.makeProductResponseViewFromProduct(product)
    }

    @Transactional
    override fun update(id: String, name: String, price: Long): ProductResponseView {
        val productFromDb = this.getProductEntity(id).apply {
            this.name = name
            this.price = price
        }
        val productSavedFromDb = this.productRepository.save(productFromDb)
        this.publishEvent(product = productSavedFromDb)

        return this.baseProductComponent.makeProductResponseViewFromProduct(productSavedFromDb)
    }

    @Transactional
    override fun updateOnlyStatus(id: String, status: ProductStatusType): ProductResponseView {
        val productFromDb = this.getProductEntity(id).apply {
            this.status = status
        }
        val productSavedFromDb = this.productRepository.save(productFromDb)
        this.publishEvent(product = productSavedFromDb)

        return this.baseProductComponent.makeProductResponseViewFromProduct(productSavedFromDb)
    }

    private fun publishEvent(product: Product) {
        try {
            this.eventPublisher.publishEvent(ProductRedisHashVO.of(product))
        } catch (ex: Exception) {
            log.error(ex) {
                "product update event publish error. product id: ${product.id}, product name: ${product.name}"
            }
            throw CacheStoreProcessException()
        }
    }

    override fun get(id: String): ProductResponseView {
        val product = this.getProductFromCacheOrDatabase(id)
        if (product.isDeletedStatus()) {
            throw InvalidProductStatusException()
        }
        return this.baseProductComponent.makeProductResponseViewFromProduct(product)
    }

    private fun getProductFromCacheOrDatabase(id: String) =
        this.getProductVOFromCache(id = id)
            .map { ProductRedisHashVO.makeEntity(it) }
            .orElseGet {
                val productEntityFromDb = this.productRepository.findById(id).orElseThrow { NotFoundProductException() }
                this.setProductCache(productEntityFromDb)
                productEntityFromDb
            }

    private fun setProductCache(product: Product) =
        redisUtils.set(prefix = CacheKey.PRODUCT, key = product.id.toString(), value = ProductRedisHashVO.of(product))

    fun getProductVOFromCache(id: String): Optional<ProductRedisHashVO> =
        redisUtils.getHashValue(key = CacheKey.PRODUCT, hashKey = id, classType = ProductRedisHashVO::class.java)

    /**
     * 상품 리스트 조회
     */
    override fun getList(cursorId: String?, page: Pageable): ProductCursorResultVO {
        val productList = if (cursorId.isNullOrBlank()) {
            this.productRepository.findAllByOrderByIdDesc(page) // 최초 조회
        } else {
            this.productRepository.findAllByIdLessThanOrderByIdDesc(id = cursorId, page = page)
        }

        var newCursorId: String? = null
        val productResponseViewList = if (productList.isEmpty()) {
            listOf()
        } else {
            newCursorId = productList[productList.size - 1].id
            productList.map { this.baseProductComponent.makeProductResponseViewFromProduct(it) }.toList()
        }

        return ProductCursorResultVO(
            productResponseViewList = productResponseViewList,
            hasNextCursor = this.hasNextCursor(id = newCursorId)
        )
    }

    private fun hasNextCursor(id: String?) =
        if (id.isNullOrBlank()) false // 조회 결과가 없는 경우
        else this.productRepository.existsByIdLessThan(id)

    private fun getProductEntity(id: String) =
        this.productRepository.findById(id).orElseThrow { NotFoundProductException() }
}