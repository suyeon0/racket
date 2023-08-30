package com.racket.cash.consume.config

import com.racket.cash.consume.vo.DeadLetterQueueVO
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer
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

    @Value("\${spring.kafka.consumer.auto-offset-reset}")
    private val autoOffSet: String? = null

    @Bean
    fun consumerFactory(): ConsumerFactory<String, String> {
        val config: MutableMap<String, Any> = HashMap()
        config[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers!!
        config[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = keyDeserializer as Any
        config[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        config[ConsumerConfig.GROUP_ID_CONFIG] = groupId!!
        config[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = autoOffSet!!
        return DefaultKafkaConsumerFactory(config)
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory()
        return factory
    }

    @Bean
    fun deadLetterQueueConsumerFactory(): ConsumerFactory<String, DeadLetterQueueVO> {
        val deserializer = JsonDeserializer<DeadLetterQueueVO>()
        deserializer.addTrustedPackages("com.racket.cash.consume.vo")

        val config: MutableMap<String, Any> = HashMap()
        config[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers!!
        config[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = autoOffSet!!
        config[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = ErrorHandlingDeserializer::class.java
        config[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = ErrorHandlingDeserializer::class.java

        return DefaultKafkaConsumerFactory(
            config,
            ErrorHandlingDeserializer(StringDeserializer()),
            ErrorHandlingDeserializer(deserializer)
        )
    }

    @Bean
    fun kafkaDLQListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, DeadLetterQueueVO> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, DeadLetterQueueVO>()
        factory.consumerFactory = deadLetterQueueConsumerFactory()
        return factory
    }


}