package info.hauu.highloadsocial.service

import org.openapi.api.DialogApiDelegate
import org.openapi.model.DialogMessage
import org.openapi.model.DialogUserIdSendPostRequest
import org.springframework.http.ResponseEntity

class DIalogService : DialogApiDelegate {

    override fun dialogUserIdListGet(userId: String): ResponseEntity<List<DialogMessage>> {
        return super.dialogUserIdListGet(userId)
    }

    override fun dialogUserIdSendPost(
        userId: String,
        dialogUserIdSendPostRequest: DialogUserIdSendPostRequest?
    ): ResponseEntity<Unit> {
        return super.dialogUserIdSendPost(userId, dialogUserIdSendPostRequest)
    }
}