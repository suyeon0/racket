package com.racket.delivery.adapter.`in`.rest

import com.racket.delivery.application.port.`in`.OptionDeliveryInformationUseCase
import com.racket.delivery.application.port.`in`.TrackDeliveryUseCase
import com.racket.delivery.common.annotation.DeliveryApiV1
import com.racket.delivery.common.enums.DeliveryCompanyType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@DeliveryApiV1
class DeliveryController(
    private val trackingDeliveryUseCase: TrackDeliveryUseCase,
    private val optionDeliveryInformationUseCase: OptionDeliveryInformationUseCase
) {

    /**
     * 상품별 배송 정보 조회
     */
    @GetMapping("/{optionId}")
    fun getDeliveryInformationByOption(@PathVariable optionId: Long) =
        ResponseEntity.ok(this.optionDeliveryInformationUseCase.getDeliveryInformationByOption(optionId = optionId))

    /**
     * 실시간 배송 조회
     */
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
