package com.racket.api.user.presentation.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.racket.share.vo.AddressVO

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserAdditionalResponseView(
    val id: Long,
    val addressVO: AddressVO?
)