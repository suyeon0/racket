package com.racket.api.product

import com.racket.api.product.catalog.ProductCatalogService
import com.racket.api.product.exception.NotFoundProductException
import com.racket.api.product.image.ProductImageService
import com.racket.api.product.image.response.ProductImageResponseView
import com.racket.api.product.option.OptionService
import com.racket.api.product.presentation.response.ProductDetailResponseView
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ProductDetailServiceImpl(
    private val productService: GetProductService,
    private val optionService: OptionService,
    private val catalogService: ProductCatalogService,
    private val imageService: ProductImageService
) : ProductDetailService {
    /**
     * 상품 상세 페이지 조회
     */
    override fun getProductDetail(productId: String) =
        try {
            // Fetch
            val product = this.productService.getProductResponseView(productId = productId)
            val options =
                this.optionService.getOptionList(productId = productId).filter { option -> !option.displayYn }.toList()
            val images: List<ProductImageResponseView> = this.imageService.getImageListByProductId(productId)

            ProductDetailResponseView(
                id = product.id,
                name = product.name,
                price = product.price,
                statusType = product.statusType,
                options = options,
                images = images
            )
        } catch (e: NotFoundProductException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found", e)
        }

    override fun getCatalog(productId: String) = this.catalogService.getCatalogByProductId(productId)

}