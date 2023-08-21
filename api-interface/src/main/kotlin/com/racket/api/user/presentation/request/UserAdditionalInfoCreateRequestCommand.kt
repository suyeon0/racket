package com.racket.api.user.presentation.request

import com.racket.share.vo.AddressVO
import io.swagger.v3.oas.annotations.media.Schema

data class UserAdditionalInfoCreateRequestCommand(
    @Schema(required = true)
    val addressVO: AddressVO
) {
    fun validate() {}

}