package com.racket.cache.consume.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.retry.annotation.EnableRetry

@EnableKafka
@EnableRetry
@Configuration
class KafkaConsumerConfig {

    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootstrapServers: String? = null

    @Value("\${spring.kafka.consumer.group-id}")
    private val groupId: String? = null

    @Value("\${spring.kafka.consumer.key-deserializer}")
    private val keyDeserializer: Any? = null

    @Value("\${spring.kafka.consumer.value-deserializer}")
    private val valueDeserializer: Any? = null

    @Value("\${spring.kafka.consumer.retry.back-off-period}")
    private val backOffPeriod: Int? = null

    @Value("\${spring.kafka.consumer.retry.max-attempts}")
    private val maxAttempts: Int? = null

    @Value("\${spring.kafka.consumer.auto-offset-reset}")
    private val autoOffSet: String? = null

    @Bean
    fun consumerConfigs(): Map<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers!!
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = keyDeserializer as Any
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = valueDeserializer as Any
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId!!
        props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = autoOffSet!!
        return props
    }
}