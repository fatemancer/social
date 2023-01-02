package info.hauu.highloadsocial.service

import org.openapi.api.UserApiDelegate
import org.openapi.model.User
import org.openapi.model.UserRegisterPost200Response
import org.openapi.model.UserRegisterPostRequest
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.context.request.NativeWebRequest
import java.util.*

class UserService(val jdbcTemplate: JdbcTemplate): UserApiDelegate {
    override fun getRequest(): Optional<NativeWebRequest> {
        return super.getRequest()
    }

    override fun userGetIdGet(id: String): ResponseEntity<User> {
        return super.userGetIdGet(id)
    }

    override fun userRegisterPost(userRegisterPostRequest: UserRegisterPostRequest?): ResponseEntity<UserRegisterPost200Response> {
        return super.userRegisterPost(userRegisterPostRequest)
    }

    override fun userSearchGet(firstName: String, lastName: String): ResponseEntity<List<User>> {
        return super.userSearchGet(firstName, lastName)
    }
}