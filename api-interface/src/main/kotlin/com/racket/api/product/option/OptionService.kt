package com.racket.api.product.option

import com.racket.api.product.domain.enums.ProductStatusType
import com.racket.api.product.option.response.OptionResponseView
import com.racket.api.product.option.response.OptionWithProductView
import org.bson.types.ObjectId

interface OptionService {

    //  - 상품 아이디로 리스트 조회
    fun getOptionList(productId: String): List<OptionResponseView>

    //  - 옵션 아이디로 단건 조회
    fun getById(optionId: String): OptionResponseView

    // 옵션 추가
    fun addOption(option: OptionCreateDTO): OptionResponseView

    // 옵션 수정
    fun patchOption(id: String, option: OptionUpdateDTO): OptionResponseView

    // 옵션 사용 여부 변경
    fun patchDisplayYn(id: String, displayYn: Boolean?): OptionResponseView

    // 옵션(+상품) 정보 조회
    fun getOptionWithProductView(optionId: String, productId: String): OptionWithProductView

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