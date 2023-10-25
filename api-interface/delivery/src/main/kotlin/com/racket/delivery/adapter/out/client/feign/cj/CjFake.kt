package com.racket.delivery.adapter.out.client.feign.cj

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.io.File

@Component
@Qualifier("cjFakeFeignClient")
class CjFakeFeignClient : CJDeliveryApiFeignClient {

    companion object {
        private val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())
        private val jsonFile = File("api-interface/delivery/src/main/resources/tracking-cj.json")
        private val fakeResponseMap = objectMapper.readValue(jsonFile, CjFakeApiResponse::class.java)
            .contents
            .associateBy { it.invoice }
    }

    override fun getTrackingDeliveryList(request: CjDeliveryApiRequest): ResponseEntity<CjDeliveryApiResponse> {
        return try {
            fakeResponseMap[request.invoiceNo] ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
            ResponseEntity.ok(fakeResponseMap[request.invoiceNo])
        } catch (e: Exception) {
            throw RuntimeException("Failed to read fake response JSON", e)
        }
    }
}

data class CjFakeApiResponse(
    val contents: List<CjDeliveryApiResponse>
)