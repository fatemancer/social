package info.hauu.highloadsocial.util

import org.springframework.security.core.context.SecurityContextHolder

class UserHolder {
}

fun currentUser(): String {
    return SecurityContextHolder.getContext().authentication.name
}