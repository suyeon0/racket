package com.racket.api.product

import com.racket.api.product.catalog.ProductCatalogService
import com.racket.api.product.catalog.response.ProductCatalogResponseView
import com.racket.api.product.exception.NotFoundProductException
import com.racket.api.product.image.ProductImageService
import com.racket.api.product.image.response.ProductImageResponseView
import com.racket.api.product.option.OptionService
import com.racket.api.product.option.response.OptionResponseView
import com.racket.api.product.presentation.response.ProductDetailResponseView
import com.racket.api.product.presentation.response.ProductResponseView
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ProductDetailServiceImpl(
    private val productService: GetProductService,
    private val optionService: OptionService,
    private val catalogService: ProductCatalogService,
    private val imageService: ProductImageService
): ProductDetailService {
    /**
     * 상품 상세 페이지 조회
     */
    override fun getProductDetail(productId: String): ProductDetailResponseView? {
        return try {
            // Fetch
            val product: ProductResponseView = this.productService.getByProductId(productId)
            val options: List<OptionResponseView> =
                this.optionService.getListByProductId(productId).stream().filter { option -> !option.displayYn }.toList()
            val catalog: ProductCatalogResponseView = this.catalogService.getCatalogByProductId(productId)
            val images: List<ProductImageResponseView> = this.imageService.getImageListByProductId(productId)

            val productDetailResponse = ProductDetailResponseView(
                id = product.id,
                name = product.name,
                price = product.price,
                statusType = product.statusType,
                options = options,
                catalog = catalog,
                images = images
            )

            productDetailResponse
        } catch (e: NotFoundProductException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found", e)
        }
    }
}