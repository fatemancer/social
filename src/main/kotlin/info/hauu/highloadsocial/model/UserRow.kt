package info.hauu.highloadsocial.model

import java.util.*

data class UserRow(
    val id: UUID,
    val firstName: String,
    val secondName: String,
    val age: Int,
    val biography: String,
    val city: String
)
