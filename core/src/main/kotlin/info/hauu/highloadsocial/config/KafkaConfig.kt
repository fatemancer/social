package info.hauu.highloadsocial.config

import info.hauu.highloadsocial.model.queue.PostEntity
import info.hauu.highloadsocial.properties.QueueProperties
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.Partitioner
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.Cluster
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
class KafkaConfig(
    final val queueProperties: QueueProperties
) {
    private val bootstrapAddress: String = queueProperties.kafkaBootstrap

    @Bean
    fun kafkaAdmin(): KafkaAdmin? {
        val configs: MutableMap<String, Any> = HashMap()
        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        return KafkaAdmin(configs)
    }

    @Bean
    fun postNotificationTopic(): NewTopic? {
        return NewTopic(queueProperties.postNotificationTopic, 1, 1.toShort())
    }

    @Bean
    fun producerFactory(): ProducerFactory<String?, String?> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.PARTITIONER_CLASS_CONFIG] = AuthorIdEvenPartitioner::class.java
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String?, String?>? {
        return KafkaTemplate(producerFactory())
    }
}

class AuthorIdEvenPartitioner : Partitioner {

    override fun configure(configs: MutableMap<String, *>?) {

    }

    override fun close() {

    }

    // при необходимости возможно масштабировать сервисы вебсокетов и делать более хитрое разбиение
    override fun partition(
        topic: String?,
        key: Any?,
        keyBytes: ByteArray?,
        value: Any?,
        valueBytes: ByteArray?,
        cluster: Cluster?
    ): Int {
        val partitions = cluster?.partitionsForTopic(topic)
        return if (key is PostEntity) {
            val authorId = key.author.toLongOrNull() ?: 0
            if (authorId % 2 == 0L)  1 else 2
        } else {
            partitions?.get(0)?.partition() ?: 1
        }
    }
}
