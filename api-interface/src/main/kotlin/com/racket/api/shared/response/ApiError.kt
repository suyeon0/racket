package com.racket.api.shared.response

import io.swagger.v3.oas.annotations.media.Schema

data class ApiError(
    @Schema(title = "Error Code", example = "400")
    val code: Int,
    @Schema(title = "Error message", example = "Bad Request")
    val message: String
)