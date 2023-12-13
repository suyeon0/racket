package com.racket.api.cash.publish

interface PublishService {

    fun send(topic: String, message: String)

}