package com.racket.delivery

import com.racket.delivery.annotation.DeliveryApiV1
import com.racket.delivery.request.TrackDeliveryRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody

@DeliveryApiV1
class DeliveryController(
    private val deliveryService: DeliveryService
) {

    /**
     * 상품별 배송 소요일 조회
     */
    @GetMapping("/{optionId}")
    fun getDeliveryDaysByOption(@PathVariable optionId: Long) =
        ResponseEntity.ok(this.deliveryService.getDeliveryDaysByOption(optionId = optionId))

    /**
     * 실시간 배송 조회
     */
    @GetMapping("/tracking")
    fun trackDelivery(@RequestBody trackDeliveryRequest: TrackDeliveryRequest) =
        this.deliveryService.trackDelivery(
            invoiceNumber = trackDeliveryRequest.invoiceNumber,
            deliveryCompany = trackDeliveryRequest.deliveryCompanyType
        )

}