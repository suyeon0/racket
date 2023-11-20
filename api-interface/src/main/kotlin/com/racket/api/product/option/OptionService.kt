package com.racket.api.product.option

import com.racket.api.product.domain.enums.ProductStatusType
import com.racket.api.product.option.reponse.OptionResponseView
import org.bson.types.ObjectId

interface OptionService {

    //  - 상품 아이디로 리스트 조회
    fun getListByProductId(productId: String): List<OptionResponseView>

    //  - 옵션 아이디로 단건 조회
    fun getByOptionId(optionId: String): OptionResponseView

    // 옵션 추가
    fun addOption(option: OptionCreateDTO): OptionResponseView

    // 옵션 수정
    fun patchOption(id: String, option: OptionUpdateDTO): OptionResponseView

    // 옵션 사용 여부 변경
    fun patchDisplayYn(id: String, displayYn: Boolean?): OptionResponseView

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

    data class OptionUpdateDTO(
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