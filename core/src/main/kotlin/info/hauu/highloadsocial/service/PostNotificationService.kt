package info.hauu.highloadsocial.service

import info.hauu.highloadsocial.model.queue.PostEntity
import info.hauu.highloadsocial.queue.PostQueueProducer
import info.hauu.highloadsocial.repository.FriendRepository
import org.springframework.stereotype.Service

@Service
class PostNotificationService(
    private val postQueue: PostQueueProducer,
    private val postCacheService: PostCacheService,
    private val friendRepository: FriendRepository
) {

    fun propagate(post: info.hauu.highloadsocial.model.domain.PostEntity, authorId: String) {
        val subscribers = friendRepository.getSubscribers(authorId)
        postCacheService.invalidateAuthorCache(authorId, subscribers)
        postQueue.send(PostEntity(post.id, post.post, post.author, subscribers))
    }
}