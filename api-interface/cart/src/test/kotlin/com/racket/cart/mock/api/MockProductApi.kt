package com.racket.cart.mock.api

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

class MockProductApi {

    companion object {
        fun setupGetOptionResponse(mockProductApi: WireMockServer) {
            mockProductApi.stubFor(
                WireMock.get(WireMock.urlPathMatching("/api/v1/product/option-product"))
                    .withQueryParam("productId", WireMock.matching("[a-f0-9]+"))
                    .withQueryParam("optionId", WireMock.matching("[a-f0-9]+"))
                    .willReturn(
                        WireMock.aResponse()
                            .withStatus(HttpStatus.OK.value())
                            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                            .withBodyFile("get-option.json")
                    )
            )

            mockProductApi.stubFor(
                WireMock.get(WireMock.urlPathMatching("/api/v1/product/option/[a-f0-9]+"))
                    .willReturn(
                        WireMock.aResponse()
                            .withStatus(HttpStatus.OK.value())
                            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                            .withBodyFile("get-option.json")
                    )
            )
        }

    }

}