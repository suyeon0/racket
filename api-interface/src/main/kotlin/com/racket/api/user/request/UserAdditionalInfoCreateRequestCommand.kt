package com.racket.api.user.request

import com.racket.api.shared.vo.AddressVO
import io.swagger.v3.oas.annotations.media.Schema

data class UserAdditionalInfoCreateRequestCommand(
    @Schema(required = true)
    val addressVO: AddressVO
) {
    fun validate() {}

}