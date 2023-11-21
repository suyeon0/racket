package com.racket.api.product.presentation

import com.racket.api.product.GetProductService
import com.racket.api.product.option.OptionService
import io.swagger.v3.oas.annotations.tags.Tag
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

    override fun getProduct(@PathVariable productId: String) = ResponseEntity.ok(this.productService.getByProductId(productId))

    override fun getOptions(@PathVariable productId: String) = ResponseEntity.ok(this.optionServiceImpl.getListByProductId(productId))

    override fun getOption(@PathVariable optionId: String) = ResponseEntity.ok(this.optionServiceImpl.getByOptionId(optionId))

    override fun getProductList(@RequestParam cursorId: String?) =
        ResponseEntity.ok(this.productService.getList(cursorId = cursorId, page = PageRequest.of(0, CURSOR_SIZE)))

    override fun getOptionWithProduct(productId: String, optionId: String) = ResponseEntity.ok(this.optionServiceImpl.getOptionWithProduct(optionId, productId))

    fun getAvailableStock(@PathVariable productId: String, @PathVariable optionId: String) =
        ResponseEntity.ok(this.productService.getByProductId(productId))

}