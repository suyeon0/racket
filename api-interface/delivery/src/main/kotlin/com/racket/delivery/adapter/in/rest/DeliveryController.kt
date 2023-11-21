package com.racket.delivery.adapter.`in`.rest

import com.racket.api.shared.annotation.SwaggerFailResponse
import com.racket.delivery.adapter.`in`.rest.response.OptionDeliveryInformationResponseView
import com.racket.delivery.adapter.`in`.rest.response.TrackingDeliveryResponseView
import com.racket.delivery.application.port.`in`.OptionDeliveryInformationUseCase
import com.racket.delivery.application.port.`in`.TrackDeliveryUseCase
import com.racket.delivery.common.annotation.DeliveryApiV1
import com.racket.delivery.common.enums.DeliveryCompanyType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Tag(name = "배송 API")
@DeliveryApiV1
class DeliveryController(
    private val trackingDeliveryUseCase: TrackDeliveryUseCase,
    private val optionDeliveryInformationUseCase: OptionDeliveryInformationUseCase
) {

    @SwaggerFailResponse
    @Operation(
        summary = "상품별 배송 정보 조회",
        description = "상품별 배송 정보 조회",
        parameters = [Parameter(name = "optionId", description = "상품 옵션 ID", example = "10")],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = OptionDeliveryInformationResponseView::class))]
            )
        ]
    )
    @GetMapping("/{optionId}")
    fun getDeliveryInformationByOption(@PathVariable optionId: Long) =
        ResponseEntity.ok(this.optionDeliveryInformationUseCase.getDeliveryInformationByOption(optionId = optionId))

    @Operation(
        summary = "실시간 배송 조회",
        description = "실시간 배송 조회",
        parameters = [
            Parameter(name = "deliveryCompanyType", description = "택배사", example = "CJ"),
            Parameter(name = "invoiceNumber", description = "송장번호", example = "123456")
                     ],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = TrackingDeliveryResponseView::class))]
            )
        ]
    )
    @GetMapping("/tracking/{deliveryCompanyType}/{invoiceNumber}")
    fun trackDelivery(
        @PathVariable deliveryCompanyType: DeliveryCompanyType,
        @PathVariable invoiceNumber: String
    ) =
        ResponseEntity.ok(
            this.trackingDeliveryUseCase.trackDelivery(
                invoiceNumber = invoiceNumber,
                deliveryCompany = deliveryCompanyType
            )
        )
}
