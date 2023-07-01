package com.racket.api.product.option

import com.racket.api.product.option.reponse.OptionResponseView

interface OptionService {

    // 옵션 리스트 조회
    // 옵션 리스트에는 옵션의 이름과 가격이 나와야 함

    //  - 상품 아이디로 리스트 조회
    fun getListByProductId(productId: Long): List<OptionResponseView>

    //  - 옵션 아이디로 단건 조회
    fun getByOptionId(optionId: Long): OptionResponseView


}