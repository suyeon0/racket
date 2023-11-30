package com.racket.api.product.presentation

import com.racket.api.product.GetProductService
import com.racket.api.product.ProductDetailService
import com.racket.api.product.exception.NotFoundProductException
import com.racket.api.product.option.OptionService
import com.racket.api.product.presentation.response.ProductDetailResponseView
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@Tag(name = "상품 API")
@RequestMapping("/api/v1/product")
class ProductApiController(
    val optionServiceImpl: OptionService,
    val productService: GetProductService,
    val productDetailService: ProductDetailService
): ProductSpecification {
    companion object {
        private const val CURSOR_SIZE = 10
    }

    @GetMapping("/detail/{productId}")
    fun getProductDetail(@PathVariable productId: String): ResponseEntity<ProductDetailResponseView> =
        try {
            ResponseEntity.ok(this.productDetailService.getProductDetail(productId))
        } catch (e: NotFoundProductException) {
            ResponseEntity<ProductDetailResponseView>(HttpStatus.NOT_FOUND)
        }

    override fun getProduct(@PathVariable productId: String) = ResponseEntity.ok(this.productService.getProductResponseView(productId))

    override fun getOptions(@PathVariable productId: String) = ResponseEntity.ok(this.optionServiceImpl.getOptionList(productId))

    override fun getOption(@PathVariable optionId: String) = ResponseEntity.ok(this.optionServiceImpl.getById(optionId))

    override fun getProductList(@RequestParam cursorId: String?) =
        ResponseEntity.ok(this.productService.getList(cursorId = cursorId, page = PageRequest.of(0, CURSOR_SIZE)))

    override fun getOptionWithProduct(productId: String, optionId: String) = ResponseEntity.ok(this.optionServiceImpl.getOptionWithProductView(optionId, productId))

    fun getAvailableStock(@PathVariable productId: String, @PathVariable optionId: String) =
        ResponseEntity.ok(this.productService.getProductResponseView(productId))

}