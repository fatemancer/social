package hauu.info.highloadsocial.websocket

import hauu.info.highloadsocial.websocket.config.QueueProperties
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload

class TopicListener(
    queueProperties: QueueProperties
) {

    //todo: в свойства
    @KafkaListener(topics = ["post-notifications"], groupId = "1")
    fun listen(@Payload message: String) {
        logger.info("Received $message from Kafka!")
    }

    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)
    }
}