package info.hauu.highloadsocial.service

import info.hauu.highloadsocial.model.SafeLoginRequest
import info.hauu.highloadsocial.repository.UserRepository
import info.hauu.highloadsocial.service.validators.Constraints
import info.hauu.highloadsocial.service.validators.hasId
import info.hauu.highloadsocial.service.validators.hasPassword
import mu.KotlinLogging
import org.openapi.api.LoginApiDelegate
import org.openapi.model.LoginPost200Response
import org.openapi.model.LoginPostRequest
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

private val logger = KotlinLogging.logger {}

@Service
class LoginService(val userRepository: UserRepository) : LoginApiDelegate {

    override fun loginPost(loginPostRequest: LoginPostRequest?): ResponseEntity<LoginPost200Response> {
        validate(loginPostRequest).apply {
            logger.debug { "Login attempt by ${this.id}" }
            return when (userRepository.login(id, password)) {
                LoginState.NOT_ALLOWED -> ResponseEntity.badRequest().build()
                LoginState.NOT_FOUND -> ResponseEntity.notFound().build()
                LoginState.SUCCESS -> ResponseEntity.ok(LoginPost200Response(createToken(id)))
            }
        }
    }

    private fun createToken(userId: String): String {
        val token = UUID.randomUUID().toString()
        userRepository.saveToken(userId, token)
        return token
    }

    private fun validate(loginPostRequest: LoginPostRequest?): SafeLoginRequest {
        Constraints(hasPassword(loginPostRequest), hasId(loginPostRequest)).test()
        return SafeLoginRequest(loginPostRequest?.id!!, loginPostRequest.password!!)
    }

    enum class LoginState {
        NOT_FOUND,
        NOT_ALLOWED,
        SUCCESS
    }
}