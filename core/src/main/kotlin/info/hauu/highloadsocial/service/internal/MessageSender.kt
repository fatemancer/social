package info.hauu.highloadsocial.service.internal

import mu.KotlinLogging
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

private val logger = KotlinLogging.logger {}

@Controller
@RequestMapping("internal/jms")
class MessageSender(val jmsTemplate: JmsTemplate) {

    @PostMapping
    fun post(@RequestBody entity: Request) {
        logger.info("Manual request $entity")
        jmsTemplate.convertAndSend(entity.queueName, entity.message)
    }
}

data class Request(
    val queueName: String,
    val message: Map<String, String>
)
