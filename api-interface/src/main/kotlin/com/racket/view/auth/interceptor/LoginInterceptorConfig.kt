package com.racket.view.auth.interceptor

import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@RequiredArgsConstructor
class LoginInterceptorConfig(private val loginInterceptor: LoginInterceptor) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(loginInterceptor)
            .addPathPatterns("/auth/user-info")
            .excludePathPatterns("/resources/static/js")
            .excludePathPatterns("/auth/logout", "/auth/login", "/")
    }
}