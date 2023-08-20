package com.racket.cash.publish

interface PublishService {

    fun send(topic: String, message: String)

}