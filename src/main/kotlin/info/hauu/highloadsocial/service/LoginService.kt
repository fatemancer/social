package info.hauu.highloadsocial.service

import org.openapi.api.LoginApiDelegate
import org.openapi.model.LoginPost200Response
import org.openapi.model.LoginPostRequest
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.web.context.request.NativeWebRequest
import java.util.*

@Service
class LoginService(val jdbcTemplate: JdbcTemplate) : LoginApiDelegate {

    override fun getRequest(): Optional<NativeWebRequest> {
        return super.getRequest()
    }

    override fun loginPost(loginPostRequest: LoginPostRequest?): ResponseEntity<LoginPost200Response> {
        return super.loginPost(loginPostRequest)
    }
}