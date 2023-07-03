package hauu.info.highloadsocial.websocket.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@EnableKafka
@Configuration
class KafkaConfig(
    val props: QueueProperties
) {

    @Bean
    fun factory() : DefaultKafkaConsumerFactory<String, String> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = props.kafkaBootstrap
        configProps[ConsumerConfig.GROUP_ID_CONFIG] = 1
        configProps[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        logger.info("Kafka factory config: $configProps")
        return DefaultKafkaConsumerFactory(configProps)
    }

    @Bean
    fun containerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val concurrentKafkaListenerContainerFactory = ConcurrentKafkaListenerContainerFactory<String, String>()
        concurrentKafkaListenerContainerFactory.consumerFactory = factory()
        return concurrentKafkaListenerContainerFactory
    }

    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)
    }
}