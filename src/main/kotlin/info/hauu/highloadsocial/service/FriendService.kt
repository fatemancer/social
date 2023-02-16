package info.hauu.highloadsocial.service

import info.hauu.highloadsocial.repository.FriendRepository
import info.hauu.highloadsocial.util.currentUser
import org.openapi.api.FriendApiDelegate
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class FriendService(
    val friendRepository: FriendRepository,
    val postCacheService: PostCacheService
) : FriendApiDelegate {

    override fun friendDeleteUserIdPut(userId: String): ResponseEntity<Unit> {
        val subscriberId = currentUser()
        val ok = ResponseEntity.ok(friendRepository.unsubscribe(subscriberId, userId))
        postCacheService.invalidateSubscriberCache(subscriberId)
        return ok
    }

    override fun friendSetUserIdPut(userId: String): ResponseEntity<Unit> {
        val subscriberId = currentUser()
        val ok = ResponseEntity.ok(friendRepository.subscribe(subscriberId, userId))
        postCacheService.invalidateSubscriberCache(subscriberId)
        return ok
    }
}