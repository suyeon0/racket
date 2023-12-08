package com.racket.api.admin.product

import com.racket.api.admin.product.request.OptionCreateRequestCommand
import com.racket.api.admin.product.request.OptionUpdateRequestCommand
import com.racket.api.admin.product.request.ProductCreateRequestCommand
import com.racket.api.admin.product.request.ProductUpdateRequestCommand
import com.racket.api.product.domain.enums.ProductStatusType
import com.racket.api.product.option.OptionService
import com.racket.api.product.option.response.OptionResponseView
import com.racket.api.product.response.ProductResponseView
import com.racket.api.product.service.ProductService
import com.racket.api.product.vo.ProductRegisterVO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "Admin - 상품 API")
@RequestMapping("/api/v1/admin/product")
class AdminProductApiController(
    val productService: ProductService,
    val optionService: OptionService
) {

    @PostMapping
    @Operation(summary = "상품 등록")
    fun postProduct(@RequestBody request: ProductCreateRequestCommand): ResponseEntity<ProductResponseView> {
        request.validate()

        val productRegisterVo = ProductRegisterVO(
            name = request.productName,
            price = request.productPrice
        )
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(this.productService.register(productRegisterVo))
    }

    @PatchMapping("/info/{id}")
    @Operation(summary = "상품 수정")
    fun patchProduct(
        @PathVariable id: String,
        @RequestBody request: ProductUpdateRequestCommand
    ): ResponseEntity<ProductResponseView> {
        request.validate()
        this.validateProductIdAndThenThrowException(id)

        return ResponseEntity.status(HttpStatus.OK)
            .body(
                this.productService.updateProductInfo(
                    id = id,
                    name = request.productName,
                    price = request.productPrice
                )
            )
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "상품 상태 변경")
    fun patchProductStatus(@PathVariable id: String, @RequestParam status: ProductStatusType) =
        ResponseEntity.status(HttpStatus.OK).body(this.productService.updateOnlyStatus(id = id, status = status))

    private fun validateProductIdAndThenThrowException(id: String) = this.productService.get(id)

    /**
     * 옵션
     */
    @Operation(summary = "옵션 등록")
    @PostMapping("/option")
    fun postOption(request: OptionCreateRequestCommand): ResponseEntity<OptionResponseView> {
        request.validate()
        this.validateProductIdAndThenThrowException(request.productId!!)

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

    @Operation(summary = "옵션 수정")
    @PatchMapping("/option/info/{id}")
    fun patchOption(id: String, request: OptionUpdateRequestCommand): ResponseEntity<OptionResponseView> {
        request.validate()
        this.validateOptionIdAndThenThrowException(id)
        this.validateProductIdAndThenThrowException(request.productId!!)

        val optionUpdateDTO = OptionService.OptionUpdateDTO(
            productId = request.productId,
            name = request.name,
            sort = request.sort,
            price = request.price,
            stock = request.stock,
            status = request.status,
            description = request.description,
            displayYn = request.displayYn
        )
        return ResponseEntity.status(HttpStatus.OK)
            .body(this.optionService.patchOption(id = id, option = optionUpdateDTO))
    }

    @Operation(summary = "옵션 사용 여부 변경")
    @PatchMapping("/option/{id}/display-flag")
    fun patchOptionDisplay(id: String, displayYn: Boolean?): ResponseEntity<OptionResponseView> {
        displayYn ?: throw IllegalArgumentException("displayYn is null.")
        this.validateOptionIdAndThenThrowException(id)

        return ResponseEntity.status(HttpStatus.OK)
            .body(this.optionService.patchDisplayYn(id = id, displayYn = displayYn))
    }

    private fun validateOptionIdAndThenThrowException(id: String) = this.optionService.getById(id)

}