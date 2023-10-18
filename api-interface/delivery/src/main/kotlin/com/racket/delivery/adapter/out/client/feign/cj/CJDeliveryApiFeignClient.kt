package com.racket.delivery.adapter.out.client.feign.cj

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import java.io.File
import java.io.IOException


@FeignClient(name = "cjDelivery", url = "\${client.serviceUrl.cj}", contextId = "cj-delivery-client")
interface CJDeliveryApiFeignClient{

    @GetMapping("/tracking")
    fun getTrackingDeliveryList(request: CjDeliveryApiRequest): ResponseEntity<CjDeliveryApiResponse>

}

@Component
@Qualifier("cjFakeFeignClient")
class CjFakeFeignClient: CJDeliveryApiFeignClient {

    private val objectMapper = ObjectMapper()

    override fun getTrackingDeliveryList(request: CjDeliveryApiRequest): ResponseEntity<CjDeliveryApiResponse> {
        return try {
            val jsonFile = File("src/main/resources/tracking-cj.json")
            val fakeResponse = objectMapper.readValue(jsonFile, CjDeliveryApiResponse::class.java)
            ResponseEntity.ok(fakeResponse)
        } catch (e: IOException) {
            throw RuntimeException("Failed to read fake response JSON", e)
        }
    }
}