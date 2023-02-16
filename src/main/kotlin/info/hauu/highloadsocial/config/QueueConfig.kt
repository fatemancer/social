package info.hauu.highloadsocial.config

import org.apache.activemq.command.ActiveMQQueue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType
import javax.jms.Queue


const val CACHE_UPDATE_QUEUE = "postCacheUpdateQueue"

@EnableJms
@Configuration
class QueueConfig {

    @Bean
    fun postCacheUpdate(): Queue? {
        return ActiveMQQueue(CACHE_UPDATE_QUEUE)
    }

    @Bean
    fun jacksonJmsMessageConverter(): MessageConverter? {
        val converter = MappingJackson2MessageConverter() //JSON type converter
        converter.setTargetType(MessageType.TEXT)
        converter.setTypeIdPropertyName("_type");
        return converter
    }
}