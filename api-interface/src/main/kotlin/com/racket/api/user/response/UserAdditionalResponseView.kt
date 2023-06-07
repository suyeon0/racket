package com.racket.api.user.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.racket.api.common.vo.AddressVO
import com.racket.api.common.vo.MobileVO

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserAdditionalResponseView(
    val id: Long,
    val mobileVO: MobileVO?,
    val addressVO: AddressVO?
) {
}