package com.racket.api.payment.account

import com.racket.api.payment.account.response.WithdrawAccountResponseView
import com.racket.api.product.presentation.response.ProductResponseView
import com.racket.api.shared.annotation.SwaggerFailResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "계좌 API")
@RequestMapping("/api/v1/payment/account")
class AccountPaymentController(
    private val accountPaymentService: AccountPaymentService
) {

    @SwaggerFailResponse
    @Operation(
        summary = "사용자가 저장한 계좌 리스트 중 결제 가능한 목록만 출력",
        description = "유저 ID로 결제 가능한 목록만 출력",
        parameters = [Parameter(name = "userId", description = "유저 ID", example = "28")],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = ProductResponseView::class))]
            )
        ]
    )
    @GetMapping("/withdrawAccountList/{userId}")
    fun getWithdrawAccountList(@PathVariable userId: Long): ResponseEntity<List<WithdrawAccountResponseView>> =
        ResponseEntity.ok()
            .body(this.accountPaymentService.getWithdrawAccountListByUserId(userId = userId))
}