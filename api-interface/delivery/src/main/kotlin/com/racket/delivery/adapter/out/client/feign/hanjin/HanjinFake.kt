package com.racket.delivery.adapter.out.client.feign.hanjin

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.io.File

@Component
@Qualifier("hanjinFakeFeignClient")
class HanjinFakeFeignClient : HanjinDeliveryApiFeignClient {

    companion object {
        private val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())
        private val jsonFile = File("api-interface/delivery/src/main/resources/tracking-hanjin.json")
        private val fakeResponseMap = objectMapper.readValue(jsonFile, HanjinFakeApiResponse::class.java)
            .contents
            .associateBy { it.invoice }
    }

    override fun getTrackingDeliveryList(request: HanjinDeliveryApiRequest): ResponseEntity<HanjinDeliveryApiResponse> {
        return try {
            fakeResponseMap[request.invoiceNo] ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
            ResponseEntity.ok(fakeResponseMap[request.invoiceNo])
        } catch (e: Exception) {
            throw RuntimeException("Failed to read fake response JSON", e)
        }
    }
}

data class HanjinFakeApiResponse(
    val contents: List<HanjinDeliveryApiResponse>
)