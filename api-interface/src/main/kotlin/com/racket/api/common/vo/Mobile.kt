package com.racket.api.common.vo

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class Mobile(
    @Column(name = "mobile")
    val number: String
) {

    /**
     * 시작은 3개의 숫자로 구성
     * 그 다음에는 3 또는 4개의 숫자로 구성
     * 마지막으로 4개의 숫자로 끝남
     */
    @JsonIgnore
    fun isNotMatchMobileNumberFormat(): Boolean {
        val pattern = Regex("^\\d{3}\\d{3,4}\\d{4}$")
        return !pattern.matches(this.number)
    }
}