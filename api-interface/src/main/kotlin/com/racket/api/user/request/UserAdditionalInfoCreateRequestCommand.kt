package com.racket.api.user.request

import com.racket.api.shared.vo.AddressVO
import com.racket.api.shared.vo.MobileVO
import com.racket.api.util.isMatchMobileNumberFormat
import io.swagger.v3.oas.annotations.media.Schema

data class UserAdditionalInfoCreateRequestCommand(
    @Schema(required = true)
    val mobileVO: MobileVO,

    @Schema(required = true)
    val addressVO: AddressVO
) {

    fun validate() {
        if (!isMatchMobileNumberFormat(this.mobileVO.number)) {
            throw IllegalArgumentException("mobile number pattern mismatch")
        }
    }

}