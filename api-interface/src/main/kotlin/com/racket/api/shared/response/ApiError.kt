package com.racket.api.shared.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

data class ApiError(
    @Schema(title = "Error Code")
    val code: Int,
    @Schema(title = "Error message")
    val message: String,
    @Schema(title = "Error timeStamp")
    val timeStamp: Instant = Instant.now()
)