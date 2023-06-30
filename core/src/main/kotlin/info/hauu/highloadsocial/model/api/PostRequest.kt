package info.hauu.highloadsocial.model.api

import org.apache.commons.lang3.StringUtils

data class PostRequest(
    var post: String?,
    var title: String = StringUtils.substring(post, 0, 50),
    var userId: String
) {
}