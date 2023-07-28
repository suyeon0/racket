package com.racket.api.shared.annotation

import com.racket.api.shared.response.ApiError
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import java.lang.annotation.Inherited

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponses(
    value = [
        ApiResponse(responseCode = "400", description = "Bad Request", content = arrayOf(Content(schema = Schema(hidden = true)))),
        ApiResponse(responseCode = "401", description = "Unauthorized", content = arrayOf(Content(schema = Schema(hidden = true)))),
        ApiResponse(responseCode = "404", description = "Not Found", content = arrayOf(Content(schema = Schema(hidden = true)))),
        ApiResponse(responseCode = "500", description = "Internal Server Error", content = arrayOf(Content(schema = Schema(hidden = true))))
    ]
)
@Inherited
annotation class SwaggerFailResponse