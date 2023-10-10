package com.racket.delivery.common.config

import org.apache.kafka.clients.producer.ProducerConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
class KafkaConfig {

    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootstrapServers: String? = null

    @Value("\${spring.kafka.producer.key-serializer}")
    private val keySerializer: Any? = null

    @Value("\${spring.kafka.producer.value-serializer}")
    private val valueSerializer: Any? = null

    @Bean
    fun producerFactory(): ProducerFactory<String, String> {
        val config: MutableMap<String, Any> = HashMap()
        config[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers!!
        config[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = keySerializer as Any
        config[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = valueSerializer as Any
        return DefaultKafkaProducerFactory(config)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, String>? {
        return KafkaTemplate(producerFactory())
    }


}