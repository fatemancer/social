package info.hauu.highloadsocial.service

import info.hauu.highloadsocial.config.AUTHOR_CACHE_UPDATE_QUEUE
import info.hauu.highloadsocial.config.POST_UPDATE_CACHE
import info.hauu.highloadsocial.config.PostCacheChunk
import info.hauu.highloadsocial.config.SUBSCRIBER_CACHE_UPDATE_QUEUE
import info.hauu.highloadsocial.repository.PostRepository
import org.openapi.model.Post
import org.springframework.cache.annotation.Cacheable
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Service

@Service
class PostCacheService(
    val postRepository: PostRepository,
    val jmsTemplate: JmsTemplate
) {

    val FEED_SIZE: Int = 1000

    @Cacheable(cacheNames = [POST_UPDATE_CACHE])
    fun getLastBatch(userId: String): List<Post>? {
        return postRepository.feed(userId, FEED_SIZE)
            .map { p -> Post(id = p.id.toString(), text = p.post, authorUserId = p.author) }
    }

    /**
     * @author пользователь, кэши чьих читателей будут сброшены пачками по 1000 пользователей
     */
    fun invalidateAuthorCache(authorId: String, subscribers: List<String>) {
        subscribers.chunked(1000) {
            jmsTemplate.convertAndSend(AUTHOR_CACHE_UPDATE_QUEUE, PostCacheChunk(authorId, it))
        }
    }

    /**
     * @subscriberId пользователь, чей кэш будет сброшен
     */
    fun invalidateSubscriberCache(subscriberId: String) {
        jmsTemplate.convertAndSend(SUBSCRIBER_CACHE_UPDATE_QUEUE, subscriberId)
    }
}