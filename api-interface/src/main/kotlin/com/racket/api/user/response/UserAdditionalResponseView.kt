package com.racket.api.user.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.racket.api.common.vo.Address
import com.racket.api.common.vo.Mobile

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserAdditionalResponseView(
    val id: Long,
    val mobile: Mobile?,
    val address: Address?
) {
}