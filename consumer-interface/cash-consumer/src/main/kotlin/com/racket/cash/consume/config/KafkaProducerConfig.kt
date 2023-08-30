package com.racket.cash.consume.config

import com.racket.cash.consume.vo.DeadLetterQueueVO
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaProducerConfig {

    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootstrapServers: String? = null

    @Bean
    fun deadLetterQueueProducerFactory(): ProducerFactory<String, DeadLetterQueueVO> {
        val serializer = JsonSerializer<DeadLetterQueueVO>()

        val config: MutableMap<String, Any> = HashMap()
        config[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers!!
        config[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        config[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = serializer.javaClass
        return DefaultKafkaProducerFactory(config)
    }

    @Bean
    fun deadLetterQueueKafkaProduceTemplate(): KafkaTemplate<String, DeadLetterQueueVO>? {
        return KafkaTemplate(deadLetterQueueProducerFactory())
    }


}