package com.racket.api.shared.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<Any> (
    val success: Boolean,
    var response: Any? = null,
    var error: ApiError? = null,
    var timestamp: Long = Instant.now().epochSecond
) {

    constructor() : this(true, null, null, Instant.now().epochSecond)

    companion object {
        fun success(response: Any) = ApiResponse(
            success = true,
            response = response,
            error = null
        )

        fun error(error: ApiError) = ApiResponse(
            success = false,
            response = null,
            error = error
        )
    }

}