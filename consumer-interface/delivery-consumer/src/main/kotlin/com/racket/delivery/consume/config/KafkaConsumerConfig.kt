package com.racket.delivery.consume.config

import com.racket.delivery.consume.vo.DeliveryApiLogPayloadVO
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@EnableKafka
@Configuration
class KafkaConsumerConfig {

    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootstrapServers: String? = null

    @Value("\${spring.kafka.consumer.group-id}")
    private val groupId: String? = null

    @Value("\${spring.kafka.consumer.key-deserializer}")
    private val keyDeserializer: Any? = null

    @Value("\${spring.kafka.consumer.auto-offset-reset}")
    private val autoOffSet: String? = null

    @Bean
    fun logConsumerFactory(): ConsumerFactory<String, DeliveryApiLogPayloadVO> {
        val config: MutableMap<String, Any> = HashMap()
        config[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers!!
        config[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = keyDeserializer as Any
        config[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = DeliveryApiLogPayloadVO::class
        config[ConsumerConfig.GROUP_ID_CONFIG] = groupId!!
        config[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = autoOffSet!!
        return DefaultKafkaConsumerFactory(config)
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, DeliveryApiLogPayloadVO> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, DeliveryApiLogPayloadVO>()
        factory.consumerFactory = logConsumerFactory()
        return factory
    }

}