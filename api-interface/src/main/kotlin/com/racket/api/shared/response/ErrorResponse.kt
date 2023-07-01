package com.racket.api.shared.response

import javax.servlet.http.HttpServletRequest
import kotlin.String

data class ErrorResponse(
    var statusCode: Int,
    var requestUrl: String,
    val requestMethod: String,
    var errorList: List<Error>
) {
    companion object {
        fun from(
            errorList: List<Error>,
            httpServletRequest: HttpServletRequest,
            statusCode: Int
        ): ErrorResponse {
            return ErrorResponse(
                errorList = errorList,
                requestUrl = httpServletRequest.requestURI,
                requestMethod = httpServletRequest.method,
                statusCode = statusCode
            )
        }
    }
}