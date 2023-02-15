package info.hauu.highloadsocial.service

import info.hauu.highloadsocial.repository.PostRepository
import mu.KotlinLogging
import org.openapi.api.PostApiDelegate
import org.openapi.model.Post
import org.openapi.model.PostCreatePostRequest
import org.openapi.model.PostUpdatePutRequest
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.math.BigDecimal

private val logger = KotlinLogging.logger {}
@Service
class PostService(
    val postRepository: PostRepository,
    val postCacheService: PostCacheService
    ) : PostApiDelegate {

    override fun postCreatePost(postCreatePostRequest: PostCreatePostRequest?): ResponseEntity<String> {
        logger.info("req: ${getRequest()}")
        val auth: Authentication = SecurityContextHolder.getContext().authentication
        println("creating post \"$postCreatePostRequest\" here for user ${auth.name}")
        return ResponseEntity.ok("done")
    }

    override fun postDeleteIdPut(id: String): ResponseEntity<Unit> {
        return super.postDeleteIdPut(id)
    }

    override fun postFeedGet(offset: BigDecimal, limit: BigDecimal): ResponseEntity<List<Post>> {
        return super.postFeedGet(offset, limit)
    }

    override fun postGetIdGet(id: String): ResponseEntity<Post> {
        return super.postGetIdGet(id)
    }

    override fun postUpdatePut(postUpdatePutRequest: PostUpdatePutRequest?): ResponseEntity<Unit> {
        return super.postUpdatePut(postUpdatePutRequest)
    }
}

@Service
class PostCacheService {


    @Cacheable(cacheNames = ["friendPosts"])
    fun getLastBatch(): List<Post>? {
        return null
    }

}
