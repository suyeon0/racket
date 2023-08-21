package com.racket.share.vo

import io.swagger.v3.oas.annotations.media.Schema
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class AddressVO(

    @Schema(title = "주소", example = "서울 강남구 테헤란로79길 6")
    @Column(name = "street")
    val street: String?,

    @Schema(title = "우편번호", example = "06158")
    @Column(name = "zip_code")
    val zipCode: String?,

    @Schema(title = "상세주소", example = "JS타워 9층")
    @Column(name = "detailed_address")
    val detailedAddress: String?

)