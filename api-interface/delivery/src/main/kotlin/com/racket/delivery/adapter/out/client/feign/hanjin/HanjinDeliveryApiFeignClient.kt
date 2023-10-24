package com.racket.delivery.adapter.out.client.feign.hanjin

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import java.io.File
import java.io.IOException

@FeignClient(name = "hanjinDelivery", url = "\${client.serviceUrl.cj}", contextId = "hanjin-delivery-client")
interface HanjinDeliveryApiFeignClient {

    @GetMapping("/tracking")
    fun getTrackingDeliveryList(request: HanjinDeliveryApiRequest): ResponseEntity<HanjinDeliveryApiResponse>

}

@Component
@Qualifier("hanjinFakeFeignClient")
class HanjinFakeFeignClient: HanjinDeliveryApiFeignClient {

    private val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    override fun getTrackingDeliveryList(request: HanjinDeliveryApiRequest): ResponseEntity<HanjinDeliveryApiResponse> {
        return try {
            val jsonFile = File("api-interface/delivery/src/main/resources/tracking-hanjin.json")
            val fakeResponse = objectMapper.readValue(jsonFile, HanjinDeliveryApiResponse::class.java)
            ResponseEntity.ok(fakeResponse)
        } catch (e: IOException) {
            throw RuntimeException("Failed to read fake response JSON", e)
        }
    }
}