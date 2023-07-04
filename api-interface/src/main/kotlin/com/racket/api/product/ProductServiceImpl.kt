package com.racket.api.product

import com.racket.api.shared.vo.CursorResultVO
import com.racket.api.product.domain.Product
import com.racket.api.product.domain.ProductRepository
import com.racket.api.product.exception.InvalidProductStatusException
import com.racket.api.product.exception.NotFoundProductException
import com.racket.api.product.response.ProductResponseView
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(
    val productRepository: ProductRepository
) : ProductService {

    /**
     * 상품 단건 조회
     */
    override fun getByProductId(productId: Long): ProductResponseView {
        val product = this.getProductEntity(productId)
        if (product.isDeletedStatus()) {
            throw InvalidProductStatusException()
        } else {
            return this.makeProductResponseViewFromProduct(product)
        }
    }

    /**
     * 상품 리스트 조회
     */
    override fun getList(cursorId: Long?, page: Pageable): CursorResultVO<ProductResponseView> {
        val productList: List<Product> = if (cursorId == null) {
            this.productRepository.findAllByOrderByIdDesc(page) // 최초 조회
        } else {
            this.productRepository.findByIdLessThanOrderByIdDesc(id = cursorId, page = page)
        }

        val productResponseViewList = ArrayList<ProductResponseView>()
        var newCursorId: Long? = null
        if (productList.isNotEmpty()) {
            for (product in productList) {
                productResponseViewList.add(this.makeProductResponseViewFromProduct(product))
            }
            newCursorId = productList[productList.size - 1].id
        }
        return CursorResultVO(values = productResponseViewList, hasNext = this.hasNext(id = newCursorId))
    }

    private fun hasNext(id: Long?): Boolean {
        if (id == null) return false // 조회 결과가 없는 경우
        return this.productRepository.existsByIdLessThan(id)
    }

    private fun getProductEntity(id: Long) = this.productRepository.findById(id).orElseThrow { NotFoundProductException() }

    private fun makeProductResponseViewFromProduct(product: Product) =
        ProductResponseView(
            id = product.id!!,
            name = product.name,
            price = product.price
        )
}