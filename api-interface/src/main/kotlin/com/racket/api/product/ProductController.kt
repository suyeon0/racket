package com.racket.api.product

import com.racket.api.shared.annotation.LongTypeIdInputValidator
import com.racket.api.shared.vo.CursorResult
import com.racket.api.product.option.OptionService
import com.racket.api.product.response.ProductResponseView
import lombok.extern.slf4j.Slf4j
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Slf4j
@RestController
@RequestMapping("/api/product")
class ProductController(
    val optionServiceImpl: OptionService,
    val productServiceImpl: ProductService
) {

    companion object {
        private const val CURSOR_SIZE = 10
    }

    // 상품 정보 조회
    @LongTypeIdInputValidator
    @GetMapping("/{productId}")
    fun getProduct(@PathVariable productId: Long) = ResponseEntity.ok(this.productServiceImpl.getByProductId(productId))

    // 상품 옵션 리스트 조회
    @LongTypeIdInputValidator
    @GetMapping("/options/{productId}")
    fun getOptions(@PathVariable productId: Long) = ResponseEntity.ok(this.optionServiceImpl.getListByProductId(productId))

    // 상품 옵션 단건 조회
    @LongTypeIdInputValidator
    @GetMapping("/option/{optionId}")
    fun getOption(@PathVariable optionId: Long) {
        ResponseEntity.ok(this.optionServiceImpl.getByOptionId(optionId))
    }

    // 상품 리스트 조회 (페이징)
    @GetMapping("/list")
    fun getProductList(@RequestParam cursorId: Long?): ResponseEntity<CursorResult<ProductResponseView>> {
        val productList: CursorResult<ProductResponseView> = this.productServiceImpl.getList(cursorId = cursorId, page = PageRequest.of(0, CURSOR_SIZE))
        return ResponseEntity.ok(productList)
    }

}