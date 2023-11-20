package com.racket.api.admin.product.presentation

import com.racket.api.admin.product.presentation.request.OptionCreateRequestCommand
import com.racket.api.admin.product.presentation.request.OptionUpdateRequestCommand
import com.racket.api.admin.product.presentation.request.ProductCreateRequestCommand
import com.racket.api.admin.product.presentation.request.ProductUpdateRequestCommand
import com.racket.api.product.domain.enums.ProductStatusType
import com.racket.api.product.option.reponse.OptionResponseView
import com.racket.api.product.presentation.response.ProductResponseView
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Admin - 상품 API")
interface AdminProductSpecification {

    @Operation(summary = "상품 등록")
    @PostMapping
    fun postProduct(@RequestBody request: ProductCreateRequestCommand): ResponseEntity<ProductResponseView>


    @Operation(summary = "상품 수정")
    @PatchMapping("/info/{id}")
    fun updateProduct(@PathVariable id: String, @RequestBody request: ProductUpdateRequestCommand): ResponseEntity<ProductResponseView>


    @Operation(summary = "상품 상태 변경")
    @PatchMapping("/{id}/status")
    fun patchStatus(@PathVariable id: String, @RequestParam status: ProductStatusType): ResponseEntity<ProductResponseView>

    @Operation(summary = "옵션 등록")
    @PostMapping("/option")
    fun postOption(@RequestBody request: OptionCreateRequestCommand): ResponseEntity<OptionResponseView>


    @Operation(summary = "옵션 수정")
    @PatchMapping("/option/info/{id}")
    fun updateProduct(@PathVariable id: String, @RequestBody request: OptionUpdateRequestCommand): ResponseEntity<OptionResponseView>
}