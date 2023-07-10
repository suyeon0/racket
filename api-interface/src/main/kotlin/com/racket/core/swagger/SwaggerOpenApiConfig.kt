package com.racket.core.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class SwaggerOpenApiConfig {

    @Bean
    fun openApi(): OpenAPI {
        val info: Info = Info().title("Racket API Docs")
            .description("Racket API Docs 입니다")
            .version("v1")
            .contact(Contact().email("1315702@gmail.com"))

        return OpenAPI()
            .components(Components())
            .info(info)
    }
}