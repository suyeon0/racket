package com.racket.api.admin.product.events

import com.racket.api.product.vo.ProductRedisHashVO
import com.racket.api.util.RedisUtils
import com.racket.core.cache.CacheKey
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ProductAdditionOrChangeEventHandler(
    private val event: ProductAdditionOrChangeEvent,
) {
    @Async("productAdditionOrChangeExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handle(productVO: ProductRedisHashVO) {

        this.event.setCache(productVO = productVO)

    }
}

@Component
class ProductAdditionOrChangeEvent(
    private val redisUtils: RedisUtils
) {

    private val log = KotlinLogging.logger { }

    // 상품 변경 정보 캐시 저장
    fun setCache(productVO: ProductRedisHashVO) {
        try {
            redisUtils.putHashValue(
                key = CacheKey.PRODUCT,
                hashKey = productVO.id.toString(),
                hashValue = productVO
            )
        } catch (e: Exception) {
            log.error { "update Product Cache Failed. -${e.message}" }
        }
    }
}