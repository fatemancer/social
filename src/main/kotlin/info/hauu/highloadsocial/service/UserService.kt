package info.hauu.highloadsocial.service

import info.hauu.highloadsocial.model.api.UserRequest
import info.hauu.highloadsocial.repository.UserRepository
import info.hauu.highloadsocial.service.validators.Constraints
import info.hauu.highloadsocial.service.validators.hasId
import info.hauu.highloadsocial.service.validators.hasPassword
import info.hauu.highloadsocial.util.toModel
import mu.KotlinLogging
import org.openapi.api.UserApiDelegate
import org.openapi.model.User
import org.openapi.model.UserRegisterPost200Response
import org.openapi.model.UserRegisterPostRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

private val logger = KotlinLogging.logger {}

@Service
class UserService(val userRepository: UserRepository) : UserApiDelegate, UserDetailsService {

    override fun userGetIdGet(id: String): ResponseEntity<User> {
        val user = userRepository.find(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.of(Optional.of(user.toModel()))
    }

    override fun userRegisterPost(userRegisterPostRequest: UserRegisterPostRequest?): ResponseEntity<UserRegisterPost200Response> {
        validate(userRegisterPostRequest).apply {
            logger.debug("Will create user {}", this)
            userRepository.save(this)
            return ResponseEntity.ok(UserRegisterPost200Response(id))
        }
    }

    private fun validate(userRegisterPostRequest: UserRegisterPostRequest?): UserRequest {
        Constraints(hasPassword(userRegisterPostRequest), hasId(userRegisterPostRequest)).test()
        return userRegisterPostRequest!!.run {
            UserRequest(
                UUID.randomUUID().toString(),
                firstName!!,
                secondName.orEmpty(),
                age,
                biography,
                city,
                password!!
            )
        }
    }

    override fun userSearchGet(firstName: String, lastName: String): ResponseEntity<List<User>> {
        val users = userRepository.find(firstName, lastName) ?: return ResponseEntity.badRequest().build()
        return ResponseEntity.of(Optional.of(users.map { u -> u.toModel() }.toList()))
    }

    fun findIdByToken(token: String): String? {
        return userRepository.findIdByToken(token)
    }
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) {
            throw UsernameNotFoundException("User is null")
        } else {
            val user = userRepository.find(username)
            if (user != null) {
                return org.springframework.security.core.userdetails.User(
                    username,
                    "",
                    mutableListOf(SimpleGrantedAuthority("REGISTERED"))
                )
            } else {
                throw UsernameNotFoundException("User was initially found but then not found. This is likely a sync issue")
            }
        }
    }
}