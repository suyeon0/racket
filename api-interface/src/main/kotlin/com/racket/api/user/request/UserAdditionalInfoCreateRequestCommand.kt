package com.racket.api.user.request

import com.racket.api.common.vo.Address
import com.racket.api.common.vo.Mobile

data class UserAdditionalInfoCreateRequestCommand(
    val mobile: Mobile?,
    val address: Address?
) {

    fun validate() {
        if (mobile?.isNotMatchMobileNumberFormat() == true) {
            throw IllegalArgumentException("mobile number pattern mismatch")
        }
    }

}