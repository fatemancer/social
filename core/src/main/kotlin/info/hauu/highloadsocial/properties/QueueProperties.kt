package info.hauu.highloadsocial.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("app.queue")
data class QueueProperties (
    val kafkaBootstrap: String,
    val postNotificationTopic: String
) {
}