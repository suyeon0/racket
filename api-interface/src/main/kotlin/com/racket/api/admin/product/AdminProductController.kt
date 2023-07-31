package com.racket.api.admin.product

import com.racket.api.admin.product.request.ProductCreateRequestCommand
import com.racket.api.admin.product.request.ProductUpdateRequestCommand
import com.racket.api.product.CreateProductService
import com.racket.api.product.UpdateProductService
import com.racket.api.product.enums.ProductStatusType
import com.racket.api.product.response.ProductResponseView
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/admin/product")
class AdminProductController(
    val updateProductService: UpdateProductService,
    val createProductService: CreateProductService
) {

    @PostMapping
    fun post(@RequestBody request: ProductCreateRequestCommand): ResponseEntity<ProductResponseView> {
        request.validate()
        val productRegisterDTO = CreateProductService.ProductRegisterDTO(
            name = request.productName,
            price = request.productPrice,
            customerProductCode = request.customerProductCode
        )
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(this.createProductService.registerProduct(productRegisterDTO))
    }

    @PatchMapping("/info/{id}")
    fun put(
        @PathVariable id: Long,
        @RequestBody request: ProductUpdateRequestCommand
    ): ResponseEntity<ProductResponseView> {
        request.validate()
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                this.updateProductService.updateProductInfo(
                    id = id,
                    name = request.productName,
                    price = request.productPrice
                )
            )
    }

    @PatchMapping("/{id}/status")
    fun patchStatus(@PathVariable id: Long, @RequestParam status: ProductStatusType): ResponseEntity<ProductResponseView> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(this.updateProductService.updateProductStatus(id = id, status = status))
    }

}