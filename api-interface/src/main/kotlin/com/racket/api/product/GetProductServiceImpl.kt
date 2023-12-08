package com.racket.api.product

import com.racket.api.product.domain.Product
import com.racket.api.product.domain.ProductRepository
import com.racket.api.product.exception.InvalidProductStatusException
import com.racket.api.product.exception.NotFoundProductException
import com.racket.api.product.presentation.response.ProductResponseView
import com.racket.api.product.vo.ProductCursorResultVO
import com.racket.api.product.vo.ProductRedisHashVO
import com.racket.api.util.RedisUtils
import com.racket.core.cache.CacheKey
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class GetProductServiceImpl(
    val productRepository: ProductRepository,
    val redisUtils: RedisUtils
) : GetProductService {

    /**
     * 상품 단건 조회
     */
    override fun getProductResponseView(productId: String): ProductResponseView {
        val product: Product = this.getProductFromCacheOrDatabase(productId)
        this.validateProductStatus(product)
        return this.makeProductResponseViewFromProduct(product)
    }

    private fun getProductFromCacheOrDatabase(productId: String): Product =
        this.getProductVOFromCache(productId = productId)
            .map { ProductRedisHashVO.makeEntity(it) }
            .orElseGet {
                val productEntity = this.getProductEntity(id = productId)
                this.setProductCache(productEntity)
                productEntity
            }

    private fun validateProductStatus(product: Product) {
        if (product.isDeletedStatus()) {
            throw InvalidProductStatusException()
        }
    }

    private fun setProductCache(product: Product) =
        redisUtils.set(prefix = CacheKey.PRODUCT, key = product.id.toString(), value = ProductRedisHashVO.of(product))

    fun getProductVOFromCache(productId: String): Optional<ProductRedisHashVO> =
        redisUtils.getHashValue(key = CacheKey.PRODUCT, hashKey = productId, classType = ProductRedisHashVO::class.java)

    /**
     * 상품 리스트 조회
     */
    override fun getList(cursorId: String?, page: Pageable): ProductCursorResultVO {
        val productList: List<Product> = if (cursorId == null) {
            this.productRepository.findAllByOrderByIdDesc(page) // 최초 조회
        } else {
            this.productRepository.findAllByIdLessThanOrderByIdDesc(id = cursorId, page = page)
        }

        var newCursorId: String? = null
        val productResponseViewList = if (productList.isEmpty()) {
            listOf()
        } else {
            newCursorId = productList[productList.size - 1].id
            productList.map { this.makeProductResponseViewFromProduct(it) }.toList()
        }

        return ProductCursorResultVO(
            productResponseViewList = productResponseViewList,
            hasNextCursor = this.hasNextCursor(id = newCursorId)
        )
    }

    private fun hasNextCursor(id: String?): Boolean {
        if (id == null) return false // 조회 결과가 없는 경우
        return this.productRepository.existsByIdLessThan(id)
    }

    private fun getProductEntity(id: String) = this.productRepository.findById(id).orElseThrow { NotFoundProductException() }
}