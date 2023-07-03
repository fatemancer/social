package info.hauu.highloadsocial.model.queue

data class PostEntity(
    val id: Long,
    val post: String,
    val author: String,
    val subscribers: List<String>
)