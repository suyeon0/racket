package com.racket.api.shared.advice

import com.racket.api.shared.response.ApiError
import com.racket.api.shared.response.ApiResponse
import org.springframework.core.MethodParameter
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@Order(1)
@RestControllerAdvice
class GlobalApiResponseHandlerAdvice : ResponseBodyAdvice<Any> {
    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean = true

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        val status = (response as ServletServerHttpResponse).servletResponse.status
        val resolve = HttpStatus.resolve(status)

        return if ((resolve != null) && resolve.is2xxSuccessful) {
            ApiResponse.success(response = body!!)
        } else {
            ApiResponse.error(error = body as ApiError)
        }
    }

}