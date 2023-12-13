package com.racket.api.delivery.adapter.out.client.feign.cj

import com.racket.api.delivery.adapter.`in`.rest.response.TrackingDeliveryResponseView
import com.racket.delivery.common.enums.DeliveryCompanyType
import com.racket.delivery.common.enums.DeliveryStatusType
import com.racket.api.delivery.common.vo.TrackingVO
import java.time.Instant

data class CjDeliveryApiResponse(

    val company: DeliveryCompanyType = DeliveryCompanyType.CJ,
    val invoice: String,
    val timeLine: List<CjTrackingVO>

)
fun CjDeliveryApiResponse.toCommonView() =
    TrackingDeliveryResponseView(
        deliveryCompany = company,
        invoiceNumber = invoice,
        timeLine = timeLine.stream().map{it.toCommonVO()}.toList()
    )


data class CjTrackingVO (

    val timestamp: Instant, // 처리일시
    val currentLocation: String, // 현재위치
    val deliveryStatus: DeliveryStatusType, // 배송상태
    val driver: String,
    val driverPhone: String

)
fun CjTrackingVO.toCommonVO() =
    TrackingVO(
        timestamp = timestamp,
        currentLocation = currentLocation,
        deliveryStatus = deliveryStatus,
        driver = driver
    )
