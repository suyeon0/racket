package com.commerce.racket.api.`interface`.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @GetMapping("/user")
    fun index() = "hello world!"
}