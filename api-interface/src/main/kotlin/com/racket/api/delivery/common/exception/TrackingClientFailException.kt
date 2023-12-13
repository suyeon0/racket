package com.racket.api.delivery.common.exception

class TrackingClientFailException() : RuntimeException() {
    override val message: String = "요청하신 정보를 불러올 수 없습니다"
}