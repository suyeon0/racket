package com.racket.api.product.option

import com.racket.api.product.domain.enums.ProductStatusType
import com.racket.api.product.option.reponse.OptionResponseView
import org.bson.types.ObjectId

interface OptionService {

    // 옵션 리스트 조회
    // 옵션 리스트에는 옵션의 이름과 가격이 나와야 함

    //  - 상품 아이디로 리스트 조회
    fun getListByProductId(productId: String): List<OptionResponseView>

    //  - 옵션 아이디로 단건 조회
    fun getByOptionId(optionId: String): OptionResponseView
    fun addOption(option: OptionCreateDTO): OptionResponseView

    data class OptionCreateDTO(
        val id: String = ObjectId().toHexString(),
        val productId: String?,
        val name: String?,
        val sort: Int?,
        val price: Long?,
        val stock: Int?,
        val status: ProductStatusType?,
        val description: String?,
        val displayYn: Boolean?
    )


}