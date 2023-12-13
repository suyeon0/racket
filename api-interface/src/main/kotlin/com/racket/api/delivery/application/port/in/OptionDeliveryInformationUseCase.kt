package com.racket.api.delivery.application.port.`in`

import com.racket.api.delivery.adapter.`in`.rest.response.OptionDeliveryInformationResponseView

interface OptionDeliveryInformationUseCase {

    /**
     * 상품 Option 별 배송 관련 정보 조회
     */
    fun getDeliveryInformationByOption(optionId: Long): OptionDeliveryInformationResponseView

}