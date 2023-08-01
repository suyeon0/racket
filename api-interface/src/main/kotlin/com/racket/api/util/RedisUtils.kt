package com.racket.api.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import mu.KotlinLogging
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.SetOperations
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.stereotype.Component
import java.util.*


@Component
class RedisUtils(
    private val redisTemplate: RedisTemplate<String, String>
) {

    private val log = KotlinLogging.logger { }
    private val objectMapper = ObjectMapper().registerModule(
        KotlinModule.Builder()
            .withReflectionCacheSize(512)
            .configure(KotlinFeature.NullToEmptyCollection, false)
            .configure(KotlinFeature.NullToEmptyMap, false)
            .configure(KotlinFeature.NullIsSameAsDefault, false)
            .configure(KotlinFeature.StrictNullChecks, false)
            .build()
    )

    private fun generateKey(prefix: String, key: String) = "${prefix}:${key}"

    /**
     * String
     */
    fun set(prefix: String, key: String, value: Any): Boolean {
        return try {
            this.redisTemplate.opsForValue()
                .set(this.generateKey(prefix = prefix, key = key), objectMapper.writeValueAsString(value))
            true
        } catch (e: Exception) {
            log.error { "Redis Save Data Exception -${e.message}" }
            false
        }
    }

    fun <T : Any> get(key: String, classType: Class<T>): Optional<T> {
        return try {
            Optional.of(objectMapper.readValue(this.redisTemplate.opsForValue().get(key), classType))

        } catch (e: Exception) {
            log.error { "Redis Get Data Exception -${e.message}" }
            Optional.empty()
        }
    }

    fun get(key: String): String? = redisTemplate.opsForValue().get(key)

    /**
     * Hash
     */
    fun putHashValue(key: String, hashKey: String, hashValue: Any): Boolean {
        return try {
            this.redisTemplate.opsForHash<String, String>().put(key, hashKey, objectMapper.writeValueAsString(hashValue))
            true
        } catch (e: Exception) {
            log.error { "Redis Set Data Exception -${e.message}" }
            false
        }
    }

    fun <T : Any> getHashValue(key: String, hashKey: String, classType: Class<T>): Optional<T> {
        return try {
            val value = this.redisTemplate.opsForHash<String, String>().get(key, hashKey)
            Optional.of(objectMapper.readValue(value, classType))

        } catch (e: Exception) {
            log.error { "Redis Get Data Exception -${e.message}" }
            Optional.empty()
        }
    }


    /**
     * Set
     */
    fun addSet(key: String, value: Any): Boolean {
        return try {
            val setOperations: SetOperations<String, String> = redisTemplate.opsForSet()
            setOperations.add(key, objectMapper.writeValueAsString(value))
            true
        } catch (e: Exception) {
            log.error { "Redis Save Data Exception -${e.message}" }
            false
        }
    }

    /**
     * Sorted Set
     */
    fun addSortedSet(key: String, value: Any, score: Double): Boolean {
        return try {
            val zSetOperations: ZSetOperations<String, String> = redisTemplate.opsForZSet()
            zSetOperations.add(key, objectMapper.writeValueAsString(value), score)
            true
        } catch (e: Exception) {
            log.error { "Redis Save Data Exception -${e.message}" }
            false
        }
    }


}