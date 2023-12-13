package com.racket.api.delivery.application.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.racket.api.delivery.adapter.`in`.rest.response.TrackingDeliveryResponseView
import com.racket.api.delivery.adapter.out.client.feign.cj.CjDeliveryApiResponse
import com.racket.api.delivery.adapter.out.client.feign.hanjin.HanjinDeliveryApiResponse
import com.racket.api.delivery.application.port.`in`.TrackDeliveryUseCase
import com.racket.api.delivery.application.port.out.databases.DeliveryApiLogRepositoryPort
import com.racket.delivery.common.enums.DeliveryCompanyType
import com.racket.api.delivery.common.vo.TrackingVO
import com.racket.api.delivery.domain.DeliveryApiLog
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.time.LocalDateTime
import java.util.*

@Service
class TrackDeliveryService(
    private val deliveryClientFactoryService: DeliveryClientFactoryService,
    private val deliveryApiLogRepositoryPort: DeliveryApiLogRepositoryPort,
    private val objectMapper: ObjectMapper
) : TrackDeliveryUseCase {

    private val log = KotlinLogging.logger { }

    override fun trackDelivery(deliveryCompany: DeliveryCompanyType, invoiceNumber: String): TrackingDeliveryResponseView {
        val recentLogOpt: Optional<DeliveryApiLog> = getRecentCallLog(
            companyType = deliveryCompany,
            invoiceNo = invoiceNumber
        )

        return if (recentLogOpt.isEmpty) {
            log.info { "1시간 이내 조회 로그 없음. API 호출." }
            val client = this.deliveryClientFactoryService.getClientAdapterByDeliveryCompanyType(deliveryCompany)
            client.call(invoiceNumber)

        } else {
            log.info { "1시간 이내 조회 로그 사용" }
            val deliveryApiLogResponseString = recentLogOpt.get().response
            val timeLine = deliveryCompany.parseTimeLine(timeLineString = deliveryApiLogResponseString)
            TrackingDeliveryResponseView(
                deliveryCompany = deliveryCompany,
                invoiceNumber = invoiceNumber,
                timeLine = timeLine
            )
        }
    }

    private fun parseTimeLine(deliveryCompany: DeliveryCompanyType, responseString: String): List<TrackingVO> {
        return when (deliveryCompany) {
            DeliveryCompanyType.HANJIN -> {
                val response = objectMapper.readValue(responseString, HanjinDeliveryApiResponse::class.java)
                response.timeLine.map { v ->
                    TrackingVO(
                        timestamp = v.timestamp,
                        currentLocation = v.currentLocation,
                        driver = v.driver,
                        deliveryStatus = v.deliveryStatus
                    )
                }
            }
            DeliveryCompanyType.CJ -> {
                val response = objectMapper.readValue(responseString, CjDeliveryApiResponse::class.java)
                response.timeLine.map { v ->
                    TrackingVO(
                        timestamp = v.timestamp,
                        currentLocation = v.currentLocation,
                        driver = v.driver,
                        deliveryStatus = v.deliveryStatus
                    )
                }
            }
            else -> throw RuntimeException("Unsupported delivery company")
        }
    }

    private fun getRecentCallLog(companyType: DeliveryCompanyType, invoiceNo: String): Optional<DeliveryApiLog> =
        this.deliveryApiLogRepositoryPort.findTop1ByCompanyTypeAndInvoiceNoAndResponseTimeBetween(
            companyType = companyType,
            invoiceNo = invoiceNo,
            start = LocalDateTime.now().minusHours(1L),
            end = LocalDateTime.now()
        )
}