package com.racket.api.common

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import java.io.IOException


@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class RetryTest {

    @Autowired
    private lateinit var service: TestRequestExternalService

    @Test
    fun `Retry Test - method 가 default Max Retry 값인 3번 호출되어야 함`() {

        // when
        try {
            service.callRequestExternalServer()
        } catch (e: IOException) {
            Assertions.assertNotNull(e)
        }
        // then
        Assertions.assertEquals(3, service.getCallCount())
    }
}

@Service
class TestRequestExternalService {

    companion object {
        var callCount = 0
    }
    @Retryable(value = [IOException::class])
    fun callRequestExternalServer(): Int {
        callCount++
        println("request external server")
        requestExternalServer()
        return callCount
    }

    // IOException 발생
    fun requestExternalServer() {
        throw IOException()
    }

    fun getCallCount() = callCount
}