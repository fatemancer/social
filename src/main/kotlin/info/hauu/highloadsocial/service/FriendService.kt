package info.hauu.highloadsocial.service

import org.openapi.api.FriendApiDelegate
import org.springframework.http.ResponseEntity

class FriendService : FriendApiDelegate {

    override fun friendDeleteUserIdPut(userId: String): ResponseEntity<Unit> {
        return super.friendDeleteUserIdPut(userId)
    }

    override fun friendSetUserIdPut(userId: String): ResponseEntity<Unit> {
        return super.friendSetUserIdPut(userId)
    }
}