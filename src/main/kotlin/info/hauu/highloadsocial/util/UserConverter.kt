package info.hauu.highloadsocial.util

import info.hauu.highloadsocial.model.UserRow
import org.openapi.model.User

fun UserRow.toModel(): User {
    return User(
        id.toString(),
        firstName,
        secondName,
        age,
        biography,
        city
    )
}