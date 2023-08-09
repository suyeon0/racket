package com.racket.service

interface ProduceService {

    fun send(topic: String, message: String)

}