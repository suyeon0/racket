package com.racket.api.product.image

import com.racket.api.product.GetProductService
import com.racket.api.product.domain.ProductImage
import com.racket.api.product.domain.ProductImageRepository
import com.racket.api.product.image.request.ProductImageCreateRequestCommand
import com.racket.api.product.image.response.ProductImageResponseView
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductImageServiceImpl(
    private val productService: GetProductService,
    private val productImageRepository: ProductImageRepository
) : ProductImageService {

    companion object {
        const val baseUrl = "resources/images/"
    }

    override fun getImageListByProductId(productId: String): List<ProductImageResponseView> {
        this.productService.getByProductId(productId)

        val images = this.productImageRepository.findByProductIdOrderByIdAsc(productId)
        return images.stream().map { image ->
            ProductImageResponseView(
                id = image.id!!,
                productId = image.productId,
                imageUrl = "${baseUrl}${image.imageUrl}"
            )
        }.toList()
    }

    @Transactional
    override fun addProductImages(request: ProductImageCreateRequestCommand): List<ProductImageResponseView> {
        val product = this.productService.getByProductId(request.productId)

        val productImages = request.imageUrls.map { imageUrl ->
            ProductImage(
                productId = product.id,
                imageUrl = imageUrl
            )
        }
        this.deleteImageByProductId(productId = product.id)
        val savedProductImages = this.productImageRepository.saveAll(productImages)

        return savedProductImages.map {
            ProductImageResponseView(
                id = it.id!!,
                productId = it.productId,
                imageUrl = it.imageUrl
            )
        }
    }

    private fun deleteImageByProductId(productId: String) {
        this.productImageRepository.deleteByProductId(productId)
    }
}