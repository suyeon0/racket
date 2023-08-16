package com.racket.cash.consume.config

import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.DltHandler
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.retrytopic.RetryTopicConfiguration
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Payload
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
    private val interval: Long? = null

    @Value("\${spring.kafka.consumer.retry.max-attempts}")
    private val maxAttempts: Long? = null

    @Value("\${spring.kafka.consumer.auto-offset-reset}")
    private val autoOffSet: String? = null

    private val log = KotlinLogging.logger { }

    @Bean
    fun consumerFactory(): ConsumerFactory<String, String> {
        val config: MutableMap<String, Any> = HashMap()
        config[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers!!
        config[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = keyDeserializer as Any
        config[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = valueDeserializer as Any
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

//    @DltHandler
//    fun consumeChargingProcess(
//        record: ConsumerRecord<String, String>,
//        @Payload(KafkaHeaders.RECEIVED_TOPIC) topic: String,
//        @Payload(KafkaHeaders.RECEIVED_PARTITION_ID) partitionId: Int,
//        @Payload(KafkaHeaders.OFFSET) offset: Byte,
//        @Payload(KafkaHeaders.EXCEPTION_MESSAGE) errorMessage: String
//    ) {
//        log.error("received message='${record.value()}' with partitionId='$partitionId', offset='$offset', topic='$topic'")
//    }


}