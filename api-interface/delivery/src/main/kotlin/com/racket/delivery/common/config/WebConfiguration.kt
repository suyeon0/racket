package com.racket.delivery.common.config

import com.racket.delivery.common.enums.DeliveryCompanyTypeEnumConverter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfiguration : WebMvcConfigurer {

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(DeliveryCompanyTypeEnumConverter())
    }
}