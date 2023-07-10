package com.racket.api.shared.response

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.time.LocalDateTime

@Schema(name = "ApiGlobalResponse",
    description = "공통 API Response")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiGlobalResponse<T>(
    @Schema(
        description = "API 호출 실행 결과\n" +
                "true:성공, response 필드와 함께 반환\n" +
                "false: 실패, error 필드와 함께 반환", example = true.toString()
    )
    val success: Boolean,

    @Schema(description = "API 응답(Response) 데이터. 값은 API 종류에 따라 개별 정의됨")
    var response: T? = null,

    @Schema(description = "오류 원인에 대한 설명")
    var error: ApiError? = null,

    @Schema(description = "응답일시")
    var timestamp: LocalDateTime = LocalDateTime.now()
) {

    constructor() : this(true, null, null, LocalDateTime.now())

    companion object {
        fun success(response: Any) = ApiGlobalResponse(
            success = true,
            response = response,
            error = null
        )

        fun error(error: ApiError) = ApiGlobalResponse(
            success = false,
            response = null,
            error = error
        )
    }

}