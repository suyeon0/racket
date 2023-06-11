package com.racket.api.auth.login.interceptor

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
            .excludePathPatterns("/view/auth/logout", "/view/auth/login", "/api/auth/login", "/")
    }
}