package com.racket.api.admin.product.presentation

import com.racket.api.admin.product.presentation.request.ProductCreateRequestCommand
import com.racket.api.admin.product.presentation.request.ProductUpdateRequestCommand
import com.racket.api.product.CreateProductService
import com.racket.api.product.UpdateProductService
import com.racket.api.product.domain.enums.ProductStatusType
import com.racket.api.product.presentation.response.ProductResponseView
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/admin/product")
class AdminProductController(
    val updateProductService: UpdateProductService,
    val createProductService: CreateProductService
): AdminProductSpecification {

    override fun post(@RequestBody request: ProductCreateRequestCommand): ResponseEntity<ProductResponseView> {
        request.validate()
        val productRegisterDTO = CreateProductService.ProductRegisterDTO(
            name = request.productName,
            price = request.productPrice
        )
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(this.createProductService.registerProduct(productRegisterDTO))
    }

    override fun updateProduct(
        @PathVariable id: String,
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

    override fun patchStatus(@PathVariable id: String, @RequestParam status: ProductStatusType): ResponseEntity<ProductResponseView> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(this.updateProductService.updateProductStatus(id = id, status = status))
    }

}