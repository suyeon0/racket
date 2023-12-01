package com.racket.api.product.image

import com.racket.api.product.image.request.ProductImageCreateRequestCommand
import com.racket.api.product.image.response.ProductImageResponseView
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@Tag(name = "상품 이미지 API")
@RequestMapping("/api/v1/product/image")
class ProductImageController(
    val productImageService: ProductImageService
) {

    @PostMapping("/{productId}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(
        summary = "상품 이미지 추가",
        description = "상품 이미지 추가",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Success",
                content = [Content(schema = Schema(implementation = ProductImageResponseView::class))]
            )
        ]
    )
    fun postImage(
        @PathVariable productId: String,
        @RequestPart(value = "multipartFiles") multipartFiles: List<MultipartFile>
    ) = ResponseEntity
            .status(HttpStatus.CREATED)
            .body(this.productImageService.addProductImages(productId = productId, imageFiles = multipartFiles))

    @Operation(
        summary = "상품 이미지 리스트 조회",
        description = "상품 이미지 리스트 조회",
        parameters = [Parameter(name = "productId", description = "상품 ID", example = "655b5c491466bd2fdddfba50")],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = ProductImageResponseView::class))]
            )
        ]
    )
    @GetMapping("/{productId}")
    fun getImagesByProductId(@PathVariable productId: String) =
        ResponseEntity.ok().body(this.productImageService.getImageListByProductId(productId = productId))
}