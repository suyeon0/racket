package com.racket.api.admin.product.presentation

import com.racket.api.admin.product.presentation.request.ProductCreateRequestCommand
import com.racket.api.admin.product.presentation.request.ProductUpdateRequestCommand
import com.racket.api.product.domain.enums.ProductStatusType
import com.racket.api.product.presentation.response.ProductResponseView
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Admin-Product API")
interface AdminProductSpecification {

    @Operation(summary = "상품 등록")
    @PostMapping
    fun post(@RequestBody request: ProductCreateRequestCommand): ResponseEntity<ProductResponseView>


    @Operation(summary = "상품 수정")
    @PatchMapping("/info/{id}")
    fun put(@PathVariable id: Long, @RequestBody request: ProductUpdateRequestCommand): ResponseEntity<ProductResponseView>


    @Operation(summary = "상품 상태 변경")
    @PatchMapping("/{id}/status")
    fun patchStatus(@PathVariable id: Long, @RequestParam status: ProductStatusType): ResponseEntity<ProductResponseView>

}