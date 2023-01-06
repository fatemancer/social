package info.hauu.highloadsocial.service

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
import org.springframework.web.context.request.NativeWebRequest
import java.util.*

private val logger = KotlinLogging.logger {}

open class UserService(val userRepository: UserRepository) : UserApiDelegate {
    override fun getRequest(): Optional<NativeWebRequest> {
        return super.getRequest()
    }

    override fun userGetIdGet(id: String): ResponseEntity<User> {
        val user = userRepository.find(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.of(Optional.of(user.toModel()))
    }

    override fun userRegisterPost(userRegisterPostRequest: UserRegisterPostRequest?): ResponseEntity<UserRegisterPost200Response> {
        validate(userRegisterPostRequest).apply {
            logger.debug("Will create user {}", this);
            userRepository.save(this)
            return ResponseEntity.ok(UserRegisterPost200Response(id.toString()))
        }
    }

    private fun validate(userRegisterPostRequest: UserRegisterPostRequest?): info.hauu.highloadsocial.model.UserInternal {
        Constraints(hasPassword(userRegisterPostRequest), hasId(userRegisterPostRequest)).test()
        return userRegisterPostRequest!!.run {
            info.hauu.highloadsocial.model.UserInternal(
                UUID.randomUUID(),
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
}