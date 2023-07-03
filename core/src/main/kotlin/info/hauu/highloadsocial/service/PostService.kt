package info.hauu.highloadsocial.service

import info.hauu.highloadsocial.model.api.PostRequest
import info.hauu.highloadsocial.model.domain.PostEntity
import info.hauu.highloadsocial.repository.PostRepository
import info.hauu.highloadsocial.util.currentUser
import mu.KotlinLogging
import org.openapi.api.PostApiDelegate
import org.openapi.model.Post
import org.openapi.model.PostCreatePostRequest
import org.openapi.model.PostUpdatePutRequest
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.stream.Collectors

private val logger = KotlinLogging.logger {}

@Service
class PostService(
    val postRepository: PostRepository,
    val postNotificationService: PostNotificationService,
    val postCacheService: PostCacheService
) : PostApiDelegate {

    override fun postCreatePost(postCreatePostRequest: PostCreatePostRequest?): ResponseEntity<String> {
        logger.info("Incoming post request: ${getRequest()}")
        val userId = currentUser()
        val postRequest = PostRequest(post = postCreatePostRequest?.text, userId = userId)
        val post = postRepository.save(postRequest)
        postNotificationService.propagate(post, userId)
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
        val foundPost = postRepository.findById(id)?.let {
            Post(id = it.id.toString(), text = it.post, authorUserId = it.author)
        } ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(foundPost)
    }

    override fun postUpdatePut(postUpdatePutRequest: PostUpdatePutRequest?): ResponseEntity<Unit> {
        val authorId = currentUser()
        if (postUpdatePutRequest == null) {
            return ResponseEntity.badRequest().build()
        }
        val update = postRepository.update(authorId, postUpdatePutRequest.id, postUpdatePutRequest.text)
        postNotificationService.propagate(
            PostEntity(
                postUpdatePutRequest.id.toLong(),
                "",
                postUpdatePutRequest.text,
                authorId
            ), authorId
        )
        return ResponseEntity.ok(update)
    }
}

