package com.racket.api.product

import com.racket.api.shared.annotation.LongTypeIdInputValidator
import com.racket.api.shared.vo.CursorResultVO
import com.racket.api.product.option.OptionService
import com.racket.api.product.response.ProductResponseView
import com.racket.api.shared.response.ApiError
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.extern.slf4j.Slf4j
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Slf4j
@RestController
@Tag(name = "상품 API")
@RequestMapping("/api/v1/product")
class ProductController(
    val optionServiceImpl: OptionService,
    val productServiceImpl: ProductService
) {
    companion object {
        private const val CURSOR_SIZE = 10
    }

    @LongTypeIdInputValidator
    @GetMapping("/{productId}")
    @Operation(
        summary = "상품 단건 조회",
        description = "상품 ID로 상품 단건 조회",
        responses = [
            ApiResponse(
                description = "Not Found Product",
                responseCode = "404",
                content = [Content(schema = Schema(implementation = ApiError::class))]
            )
        ]
    )
    fun getProduct(@PathVariable productId: Long) = ResponseEntity.ok(this.productServiceImpl.getByProductId(productId))

    @LongTypeIdInputValidator
    @GetMapping("/options/{productId}")
    @Operation(
        summary = "상품 옵션 리스트 조회",
        description = "상품 ID로 상품 옵션 리스트 조회",
        responses = [
            ApiResponse(
                description = "Not Found Product",
                responseCode = "404",
                content = [Content(schema = Schema(implementation = ApiError::class))]
            ),
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = ProductResponseView::class))
                    )]
            )
        ]
    )
    fun getOptions(@PathVariable productId: Long) = ResponseEntity.ok(this.optionServiceImpl.getListByProductId(productId))

    @LongTypeIdInputValidator
    @GetMapping("/option/{optionId}")
    @Operation(
        summary = "상품 옵션 단건 조회",
        description = "옵션 ID로 옵션 단건 조회",
        responses = [
            ApiResponse(
                description = "Not Found Option",
                responseCode = "404",
                content = [Content(schema = Schema(implementation = ApiError::class))]
            )
        ]
    )
    fun getOption(@PathVariable optionId: Long) = ResponseEntity.ok(this.optionServiceImpl.getByOptionId(optionId))

    @GetMapping("/list")
    @Parameter(name = "cursorId", description = "조회할 페이지 번호")
    @Operation(
        summary = "상품 리스트 조회 (페이징)",
        description = "cursor ID 에 해당하는 페이지 내 상품 리스트 리턴. 최초 조회시 cursorId null",
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = CursorResultVO::class))
                    )]
            )
        ]
    )
    fun getProductList(@RequestParam cursorId: Long?) =
        ResponseEntity.ok(this.productServiceImpl.getList(cursorId = cursorId, page = PageRequest.of(0, CURSOR_SIZE)))

}