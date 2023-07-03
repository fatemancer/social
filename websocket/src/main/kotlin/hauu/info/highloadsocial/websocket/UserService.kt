package hauu.info.highloadsocial.websocket

import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

// в перспективе здесь мог бы быть внешний сервис аутентификации вроде keycloak, чтобы не разделять базу
@Service
class UserService(
    val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findUserTokenByUserId(username)?.let {
            User(it.userId, "", mutableListOf(SimpleGrantedAuthority("REGISTERED")))
        } ?: throw IllegalArgumentException("No user found")
    }

    fun findIdByToken(token: String): String {
        return userRepository.findUserByToken(token)?.userId ?: throw IllegalArgumentException("No user with token $token")
    }
}

@Component
interface UserRepository : CrudRepository<UserToken, String> {

    fun findUserByToken(token: String): UserToken?

    fun findUserTokenByUserId(userId: String): UserToken?
}

@Table("user_credentials")
data class UserToken(
    var userId: String,
    var token: String
)