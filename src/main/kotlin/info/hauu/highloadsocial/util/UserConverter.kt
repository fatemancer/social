package info.hauu.highloadsocial.util

import info.hauu.highloadsocial.model.domain.UserEntity
import org.openapi.model.User

fun UserEntity.toModel(): User {
    return User(
        id.toString(),
        firstName,
        secondName,
        age,
        biography,
        city
    )
}