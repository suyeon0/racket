package com.racket.delivery.common.enums

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.racket.api.delivery.adapter.out.client.feign.cj.CjDeliveryApiResponse
import com.racket.api.delivery.adapter.out.client.feign.hanjin.HanjinDeliveryApiResponse
import com.racket.api.delivery.common.vo.TrackingVO

enum class DeliveryCompanyType {
    CJ {
        override fun parseTimeLine(timeLineString: String): List<TrackingVO> {
            val response = this.objectMapper.readValue(timeLineString, CjDeliveryApiResponse::class.java)
            return response.timeLine.map { v ->
                TrackingVO(
                    timestamp = v.timestamp,
                    currentLocation = v.currentLocation,
                    driver = v.driver,
                    deliveryStatus = v.deliveryStatus
                )
            }
        }
    },
    HANJIN {
        override fun parseTimeLine(timeLineString: String): List<TrackingVO> {
            val response = this.objectMapper.readValue(timeLineString, HanjinDeliveryApiResponse::class.java)
            return response.timeLine.map { v ->
                TrackingVO(
                    timestamp = v.timestamp,
                    currentLocation = v.currentLocation,
                    driver = v.driver,
                    deliveryStatus = v.deliveryStatus
                )
            }
        }
    };

    abstract fun parseTimeLine(timeLineString: String): List<TrackingVO>

    val objectMapper: ObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

}