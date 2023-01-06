package info.hauu.highloadsocial.service

import de.nycode.bcrypt.hash
import info.hauu.highloadsocial.model.SafeLoginRequest
import info.hauu.highloadsocial.service.validators.Constraints
import info.hauu.highloadsocial.service.validators.hasId
import info.hauu.highloadsocial.service.validators.hasPassword
import mu.KotlinLogging
import org.openapi.api.LoginApiDelegate
import org.openapi.model.LoginPost200Response
import org.openapi.model.LoginPostRequest
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.jdbc.core.SingleColumnRowMapper
import org.springframework.stereotype.Service
import org.springframework.web.context.request.NativeWebRequest
import java.util.*

private val logger = KotlinLogging.logger {}

@Service
class LoginService(val jdbcTemplate: JdbcTemplate) : LoginApiDelegate {

    override fun getRequest(): Optional<NativeWebRequest> {
        return super.getRequest()
    }

    override fun loginPost(loginPostRequest: LoginPostRequest?): ResponseEntity<LoginPost200Response> {
        validate(loginPostRequest).apply {
            val rm = SingleColumnRowMapper(ByteArray::class.java)
            val ps = PreparedStatementSetter {
                it.setString(1, id)
            }
            val bcrypt = jdbcTemplate.query("SELECT bcrypt FROM user_credentials WHERE user_id = ?", ps, rm)
            if (bcrypt.isEmpty()) {
                logger.warn { "User {} not found".format(id) }
                return ResponseEntity.notFound().build()
            }
            if (!bcrypt[0].contentEquals(encryptedPass)) {
                return ResponseEntity.ok(LoginPost200Response(createToken()))
            } else {
                return ResponseEntity.badRequest().build()
            }
        }
    }

    private fun createToken(): String {
        return UUID.randomUUID().toString()
    }

    private fun validate(loginPostRequest: LoginPostRequest?): SafeLoginRequest {
        Constraints(hasPassword(loginPostRequest), hasId(loginPostRequest)).test()
        return SafeLoginRequest(loginPostRequest?.id!!, hash(loginPostRequest.password!!))
    }
}