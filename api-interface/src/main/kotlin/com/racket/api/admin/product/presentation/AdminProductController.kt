package com.racket.api.admin.product.presentation

import com.racket.api.admin.product.presentation.request.OptionCreateRequestCommand
import com.racket.api.admin.product.presentation.request.OptionUpdateRequestCommand
import com.racket.api.admin.product.presentation.request.ProductCreateRequestCommand
import com.racket.api.admin.product.presentation.request.ProductUpdateRequestCommand
import com.racket.api.product.CreateProductService
import com.racket.api.product.GetProductService
import com.racket.api.product.UpdateProductService
import com.racket.api.product.domain.enums.ProductStatusType
import com.racket.api.product.exception.NotFoundProductException
import com.racket.api.product.option.OptionService
import com.racket.api.product.option.reponse.OptionResponseView
import com.racket.api.product.presentation.response.ProductResponseView
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/admin/product")
class AdminProductController(
    val updateProductService: UpdateProductService,
    val createProductService: CreateProductService,
    val getProductService: GetProductService,
    val optionService: OptionService
): AdminProductSpecification {

    override fun postProduct(@RequestBody request: ProductCreateRequestCommand): ResponseEntity<ProductResponseView> {
        request.validate()
        val productRegisterDTO = CreateProductService.ProductRegisterDTO(
            name = request.productName,
            price = request.productPrice
        )
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(this.createProductService.registerProduct(productRegisterDTO))
    }

    override fun postOption(request: OptionCreateRequestCommand): ResponseEntity<OptionResponseView> {
        request.validate()
        this.getProductService.getByProductId(request.productId!!)

        val optionRegisterDTO = OptionService.OptionCreateDTO(
            productId = request.productId,
            name = request.name,
            sort = request.sort,
            price = request.price,
            stock = request.stock,
            status = request.status,
            description = request.description,
            displayYn = request.displayYn
        )
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(this.optionService.addOption(optionRegisterDTO))
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

    override fun updateProduct(id: String, request: OptionUpdateRequestCommand): ResponseEntity<OptionResponseView> {
        TODO("Not yet implemented")
    }

    override fun patchStatus(@PathVariable id: String, @RequestParam status: ProductStatusType): ResponseEntity<ProductResponseView> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(this.updateProductService.updateProductStatus(id = id, status = status))
    }

}