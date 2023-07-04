package com.racket.api.utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.racket.api.shared.response.ApiResponse
import com.racket.api.user.response.UserResponseView
import org.springframework.test.web.servlet.MvcResult

class ObjectMapperUtils {

    companion object {
        val objectMapper = jacksonObjectMapper()

        fun resultToApiResponse(mvcResult: MvcResult): ApiResponse<*> {
            val response: String = mvcResult.response.contentAsString
            return objectMapper.registerModule(JavaTimeModule()).readValue(response, ApiResponse::class.java)
        }

        inline fun <reified T> responseToResultView(resultResponse: ApiResponse<T>): T {
            return try {
                val jsonString = objectMapper.writeValueAsString(resultResponse.response)
                objectMapper.registerModule(JavaTimeModule()).readValue(jsonString, object : TypeReference<T>() {})
            } catch (e: Exception) {
                throw Exception()
            }
        }
    }
}