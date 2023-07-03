package hauu.info.highloadsocial.websocket

import hauu.info.highloadsocial.websocket.config.QueueProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(QueueProperties::class)
class WebsocketApp

fun main(args: Array<String>) {
    runApplication<WebsocketApp>(*args)
}