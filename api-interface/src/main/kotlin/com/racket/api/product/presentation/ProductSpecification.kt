package com.racket.api.product.presentation

import com.racket.api.product.option.reponse.OptionResponseView
import com.racket.api.product.presentation.response.ProductResponseView
import com.racket.api.product.vo.ProductCursorResultVO
import com.racket.api.shared.annotation.LongTypeIdInputValidator
import com.racket.api.shared.annotation.SwaggerFailResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

@Tag(name = "Product-API", description = "상품/옵션 API")
interface ProductSpecification {

    @LongTypeIdInputValidator
    @GetMapping("/{productId}")
    @SwaggerFailResponse
    @Operation(
        summary = "상품 단건 조회",
        description = "상품 ID로 상품 단건 조회",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = ProductResponseView::class))]
            )
        ]
    )
    fun getProduct(@PathVariable productId: Long): ResponseEntity<ProductResponseView>

    @LongTypeIdInputValidator
    @GetMapping("/options/{productId}")
    @SwaggerFailResponse
    @Operation(
        summary = "상품 옵션 리스트 조회",
        description = "상품 ID로 상품 옵션 리스트 조회",
        parameters = [Parameter(name = "productId", description = "상품 ID", example = "10")],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = OptionResponseView::class))]
            )
        ]
    )
    fun getOptions(@PathVariable productId: Long): ResponseEntity<List<OptionResponseView>>

    @LongTypeIdInputValidator
    @GetMapping("/option/{optionId}")
    @SwaggerFailResponse
    @Operation(
        summary = "상품 옵션 단건 조회",
        description = "옵션 ID로 옵션 단건 조회",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = OptionResponseView::class))]
            ),
            ApiResponse(responseCode = "400", description = "Bad Request"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Not Found Option"),
            ApiResponse(responseCode = "500", description = "Internal Server Error")
        ]
    )
    fun getOption(@PathVariable optionId: Long): ResponseEntity<OptionResponseView>

    @GetMapping("/list")
    @SwaggerFailResponse
    @Parameter(name = "cursorId", description = "조회할 페이지 번호")
    @Operation(
        summary = "상품 리스트 조회 (페이징)",
        description = "cursor ID 에 해당하는 페이지 내 상품 리스트 리턴. 최초 조회시 cursorId null",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(array = ArraySchema(schema = Schema(implementation = ProductCursorResultVO::class)))]
            ),
            ApiResponse(responseCode = "400", description = "Bad Request"),
            ApiResponse(responseCode = "404", description = "Not Found"),
            ApiResponse(responseCode = "500", description = "Internal Server Error")
        ]
    )
    fun getProductList(@RequestParam cursorId: Long?): ResponseEntity<ProductCursorResultVO>
}