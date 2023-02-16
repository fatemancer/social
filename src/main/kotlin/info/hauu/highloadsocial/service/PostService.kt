package info.hauu.highloadsocial.service

import info.hauu.highloadsocial.model.api.PostRequest
import info.hauu.highloadsocial.repository.PostRepository
import info.hauu.highloadsocial.util.currentUser
import mu.KotlinLogging
import org.openapi.api.PostApiDelegate
import org.openapi.model.Post
import org.openapi.model.PostCreatePostRequest
import org.openapi.model.PostUpdatePutRequest
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.math.BigDecimal

private val logger = KotlinLogging.logger {}
@Service
class PostService(
    val postRepository: PostRepository,
    val postCacheService: PostCacheService
    ) : PostApiDelegate {

    override fun postCreatePost(postCreatePostRequest: PostCreatePostRequest?): ResponseEntity<String> {
        logger.info("Incoming post request: ${getRequest()}")
        val postRequest = PostRequest(post = postCreatePostRequest?.text, userId = currentUser())
        postRepository.save(postRequest)
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
        // лимитируем здесь, т.к. кэш дешёвый и лучше один раз сохранить последнюю 1000,
        // чем забивать кэш записями на разное число постов
        return ResponseEntity.ok(postCacheService.getLastBatch(currentUser()).orEmpty().subList(0, limit.toInt()))
    }

    override fun postGetIdGet(id: String): ResponseEntity<Post> {
        return super.postGetIdGet(id)
    }

    override fun postUpdatePut(postUpdatePutRequest: PostUpdatePutRequest?): ResponseEntity<Unit> {
        return super.postUpdatePut(postUpdatePutRequest)
    }
}

@Service
class PostCacheService(
    val postRepository: PostRepository
) {

    val FEED_SIZE: Int = 1000

    @Cacheable(cacheNames = ["friendPosts"])
    fun getLastBatch(userId: String): List<Post>? {
        return postRepository.feed(userId, FEED_SIZE)
            .map { p -> Post(id = p.id.toString(), text = p.post, authorUserId = p.author) }
    }

}
