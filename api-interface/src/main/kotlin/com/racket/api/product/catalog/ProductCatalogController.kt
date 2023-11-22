package com.racket.api.product.catalog

import com.racket.api.product.catalog.request.ProductCatalogCreateRequestCommand
import com.racket.api.product.catalog.response.ProductCatalogResponseView
import com.racket.api.product.image.response.ProductImageResponseView
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "상품 카탈로그 API")
@RequestMapping("/api/v1/product/catalog")
class ProductCatalogController(
    val productCatalogService: ProductCatalogService
) {

    @PostMapping
    @Operation(
        summary = "상품 카탈로그 추가",
        description = "상품 카탈로그 추가",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Success",
                content = [Content(schema = Schema(implementation = ProductImageResponseView::class))]
            )
        ]
    )
    fun postProductCatalog(@RequestBody request: ProductCatalogCreateRequestCommand): ResponseEntity<ProductCatalogResponseView> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(this.productCatalogService.addProductCatalog(
                productId = request.productId,
                contents = request.contents)
            )
    }

    @Operation(
        summary = "상품 카탈로그 조회",
        description = "상품 카탈로그 조회",
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
    fun getProductCatalog(@PathVariable productId: String): ResponseEntity<ProductCatalogResponseView> {
        return ResponseEntity.ok().body(this.productCatalogService.getCatalogByProductId(productId = productId))
    }

}