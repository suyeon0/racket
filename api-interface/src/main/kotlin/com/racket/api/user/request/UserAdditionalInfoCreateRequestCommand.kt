package com.racket.api.user.request

import com.racket.api.common.vo.AddressVO
import com.racket.api.common.vo.MobileVO
import com.racket.api.util.isMatchMobileNumberFormat

data class UserAdditionalInfoCreateRequestCommand(
    val mobileVO: MobileVO,
    val addressVO: AddressVO
) {

    fun validate() {
        if (!isMatchMobileNumberFormat(this.mobileVO.number)) {
            throw IllegalArgumentException("mobile number pattern mismatch")
        }
    }

}