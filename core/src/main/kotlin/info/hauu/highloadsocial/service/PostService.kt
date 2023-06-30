package info.hauu.highloadsocial.service

import info.hauu.highloadsocial.config.AUTHOR_CACHE_UPDATE_QUEUE
import info.hauu.highloadsocial.config.POST_UPDATE_CACHE
import info.hauu.highloadsocial.config.PostCacheChunk
import info.hauu.highloadsocial.config.SUBSCRIBER_CACHE_UPDATE_QUEUE
import info.hauu.highloadsocial.model.api.PostRequest
import info.hauu.highloadsocial.repository.FriendRepository
import info.hauu.highloadsocial.repository.PostRepository
import info.hauu.highloadsocial.util.currentUser
import mu.KotlinLogging
import org.openapi.api.PostApiDelegate
import org.openapi.model.Post
import org.openapi.model.PostCreatePostRequest
import org.openapi.model.PostUpdatePutRequest
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.ResponseEntity
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.stream.Collectors

private val logger = KotlinLogging.logger {}

@Service
class PostService(
    val postRepository: PostRepository,
    val postCacheService: PostCacheService
) : PostApiDelegate {

    override fun postCreatePost(postCreatePostRequest: PostCreatePostRequest?): ResponseEntity<String> {
        logger.info("Incoming post request: ${getRequest()}")
        val userId = currentUser()
        val postRequest = PostRequest(post = postCreatePostRequest?.text, userId = userId)
        postRepository.save(postRequest)
        postCacheService.invalidateAuthorCache(userId)
        logger.info("Created post \"$postRequest\"")
        return ResponseEntity.ok("Post saved")
    }

    override fun postDeleteIdPut(id: String): ResponseEntity<Unit> {
        val currentUser = currentUser()
        postRepository.delete(id.toLong(), currentUser)
        logger.info("Deleted post $id if $currentUser")
        return ResponseEntity.ok().build()
    }

    override fun postFeedGet(offset: BigDecimal, limit: BigDecimal): ResponseEntity<List<Post>> {
        val currentUser = currentUser()
        logger.info("Requesting feed for $currentUser with limit $limit offset $offset")
        val posts = postCacheService.getLastBatch(currentUser).orEmpty()
        return ResponseEntity.ok(
            posts.stream().skip(offset.toLong()).limit(limit.toLong()).collect(Collectors.toList())
        )
    }

    override fun postGetIdGet(id: String): ResponseEntity<Post> {
        val foundPost = postRepository.findById(id)?.map {
            Post(id = it.id.toString(), text = it.post, authorUserId = it.author)
        }?.firstOrNull() ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(foundPost)
    }

    override fun postUpdatePut(postUpdatePutRequest: PostUpdatePutRequest?): ResponseEntity<Unit> {
        val authorId = currentUser()
        if (postUpdatePutRequest == null) {
            return ResponseEntity.badRequest().build()
        }
        val update = postRepository.update(authorId, postUpdatePutRequest.id, postUpdatePutRequest.text)
        postCacheService.invalidateAuthorCache(authorId)
        return ResponseEntity.ok(update)
    }
}

@Service
class PostCacheService(
    val postRepository: PostRepository,
    val friendRepository: FriendRepository,
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
    fun invalidateAuthorCache(authorId: String) {
        val subscribers = friendRepository.getSubscribers(authorId)
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
