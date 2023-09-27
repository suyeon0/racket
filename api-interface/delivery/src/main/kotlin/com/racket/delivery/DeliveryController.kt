package com.racket.delivery

import com.racket.delivery.annotation.DeliveryApiV1
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

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

    fun getDeliveryInformation() =
        this.deliveryService.getDeliveryInformation(invoiceNumber = "", deliveryCompany = "")

}