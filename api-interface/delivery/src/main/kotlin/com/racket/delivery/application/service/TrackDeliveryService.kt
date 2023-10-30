package com.racket.delivery.application.service

import com.racket.delivery.adapter.`in`.rest.response.TrackingDeliveryResponseView
import com.racket.delivery.application.port.`in`.TrackDeliveryUseCase
import com.racket.delivery.application.port.out.databases.DeliveryApiLogRepositoryPort
import com.racket.delivery.common.enums.DeliveryCompanyType
import com.racket.delivery.common.vo.TrackingVO
import com.racket.delivery.domain.DeliveryApiLog
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class TrackDeliveryService(
    private val deliveryClientFactoryService: DeliveryClientFactoryService,
    private val deliveryApiLogRepositoryPort: DeliveryApiLogRepositoryPort
) : TrackDeliveryUseCase {

    override fun trackDelivery(deliveryCompany: DeliveryCompanyType, invoiceNumber: String): TrackingDeliveryResponseView {
        // 1시간 이내 조회 로그 있는지 확인
        val recentLogOpt = this.getRecentCallLog(
            companyType = deliveryCompany,
            invoiceNo = invoiceNumber
        )

        return if (recentLogOpt.isEmpty) {
            val client = this.deliveryClientFactoryService.getClientAdapterByDeliveryCompanyType(deliveryCompany)
            client.call(invoiceNumber)
        } else {
            TrackingDeliveryResponseView(
                deliveryCompany = deliveryCompany,
                invoiceNumber = invoiceNumber,
                timeLine = recentLogOpt.get().response as List<TrackingVO>
            )
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