package info.hauu.highloadsocial.service

import info.hauu.highloadsocial.repository.DialogRepository
import info.hauu.highloadsocial.util.currentUser
import org.openapi.api.DialogApiDelegate
import org.openapi.model.DialogMessage
import org.openapi.model.DialogUserIdSendPostRequest
import org.springframework.http.ResponseEntity

class DialogService(val dialogRepository: DialogRepository) : DialogApiDelegate {

    override fun dialogUserIdListGet(userId: String): ResponseEntity<List<DialogMessage>> {
        val result = dialogRepository.retrieve(currentUser(), userId)
        return ResponseEntity.ok(result.map { DialogMessage(it.initiatorId, it.receiverId, it.text) }.toList())
    }

    override fun dialogUserIdSendPost(
        userId: String,
        dialogUserIdSendPostRequest: DialogUserIdSendPostRequest?
    ): ResponseEntity<Unit> {
        if (dialogUserIdSendPostRequest?.text == null) {
            return ResponseEntity.badRequest().build()
        }
        return(ResponseEntity.ok(dialogRepository.write(currentUser(), userId, dialogUserIdSendPostRequest.text)))
    }
}