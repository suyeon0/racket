package com.racket.api.util

fun isMatchMobileNumberFormat(number: String): Boolean {
    val pattern = Regex("^\\d{3}\\d{3,4}\\d{4}$")
    return pattern.matches(number)
}