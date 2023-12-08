package com.racket.api.product.image

import com.racket.api.product.domain.ProductImage
import com.racket.api.product.domain.ProductImageRepository
import com.racket.api.product.image.response.ProductImageResponseView
import com.racket.api.product.service.ProductBaseService
import com.racket.api.product.service.ProductService
import com.racket.api.shared.service.file.FileService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class ProductImageServiceImpl(
    private val productBaseService: ProductBaseService,
    private val productImageRepository: ProductImageRepository,
    private val fileService: FileService,
    @Value("\${uploadPath}") private val uploadPath: String
) : ProductImageService {

    override fun getImageListByProductId(productId: String): List<ProductImageResponseView> {
        this.productBaseService.get(productId)

        val images = this.productImageRepository.findByProductIdOrderByIdAsc(productId)
        return images.stream().map { image ->
            ProductImageResponseView(
                id = image.id,
                productId = image.productId,
                originFileName = image.originFileName
            )
        }.toList()
    }

    @Transactional
    override fun addProductImages(productId: String, imageFiles: List<MultipartFile>): List<ProductImageResponseView> {
        this.productBaseService.get(productId)

        // file 저장
        this.saveFile(imageFiles)

        // DB 반영
        this.deleteImage(productId = productId)
        val productImages = imageFiles.map { image ->
            ProductImage(
                id = ObjectId().toHexString(),
                productId = productId,
                originFileName = image.originalFilename.toString()
            )
        }
        val savedProductImages = this.productImageRepository.saveAll(productImages)

        return savedProductImages.map {
            ProductImageResponseView(
                id = it.id,
                productId = it.productId,
                originFileName = it.originFileName
            )
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun saveFile(imageFiles: List<MultipartFile>) {
        for (file in imageFiles) {
            this.fileService.uploadFile(
                uploadPath = uploadPath,
                fileName = file.originalFilename.toString(),
                fileData = file.bytes
            )
        }
    }

    @Transactional
    private fun deleteImage(productId: String) {
        this.productImageRepository.deleteByProductId(productId)
    }
}