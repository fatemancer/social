package info.hauu.highloadsocial.model.domain

import java.util.*

data class UserEntity(
    val id: UUID,
    val firstName: String,
    val secondName: String,
    val age: Int,
    val biography: String,
    val city: String
)
