package com.racket.api.product.presentation

import com.racket.api.product.GetProductService
import com.racket.api.shared.annotation.LongTypeIdInputValidator
import com.racket.api.product.option.OptionService
import com.racket.api.product.option.reponse.OptionResponseView
import com.racket.api.product.presentation.response.ProductResponseView
import com.racket.api.product.vo.ProductCursorResultVO
import com.racket.api.shared.annotation.SwaggerFailResponse
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

@RestController
@Tag(name = "상품 API")
@RequestMapping("/api/v1/product")
class ProductController(
    val optionServiceImpl: OptionService,
    val productService: GetProductService
): ProductSpecification {
    companion object {
        private const val CURSOR_SIZE = 10
    }

    override fun getProduct(@PathVariable productId: Long) = ResponseEntity.ok(this.productService.getByProductId(productId))

    override fun getOptions(@PathVariable productId: Long) = ResponseEntity.ok(this.optionServiceImpl.getListByProductId(productId))

    override fun getOption(@PathVariable optionId: Long) = ResponseEntity.ok(this.optionServiceImpl.getByOptionId(optionId))

    override fun getProductList(@RequestParam cursorId: Long?) =
        ResponseEntity.ok(this.productService.getList(cursorId = cursorId, page = PageRequest.of(0, CURSOR_SIZE)))

    fun getAvailableStock(@PathVariable productId: Long, @PathVariable optionId: Long) =
        ResponseEntity.ok(this.productService.getByProductId(productId))

}