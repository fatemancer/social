package info.hauu.highloadsocial.queue

import info.hauu.highloadsocial.model.queue.PostEntity
import info.hauu.highloadsocial.properties.QueueProperties
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class PostQueueProducer(
    val kafkaTemplate: KafkaTemplate<String, PostEntity>,
    val queueProperties: QueueProperties
) {
    fun send(post: PostEntity) {
        kafkaTemplate.send(queueProperties.postNotificationTopic, post)
    }
}