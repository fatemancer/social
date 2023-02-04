package info.hauu.highloadsocial.repository

import de.nycode.bcrypt.hash
import de.nycode.bcrypt.verify
import info.hauu.highloadsocial.model.UserInternal
import info.hauu.highloadsocial.model.UserRow
import info.hauu.highloadsocial.service.LoginService
import info.hauu.highloadsocial.service.internal.FeatureService
import mu.KotlinLogging
import org.springframework.jdbc.core.*
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.Statement

private val logger = KotlinLogging.logger {}

@Component
class UserRepository(
        val jdbcTemplate: JdbcTemplate,
        val jdbcReplicaTemplate: JdbcTemplate,
        val featureService: FeatureService
) {

    val userMapper = DataClassRowMapper(UserRow::class.java)
    val userQuery = """
         SELECT u.id as id, first_name, second_name, age, t.tag_value as biography, city
         FROM users AS u
         JOIN user_tags AS ut ON ut.user_id = u.id
         JOIN tags AS t ON t.id = ut.tag_id AND t.tag_type = 'bio'
         JOIN location AS l ON l.user_id = u.id
         WHERE 1 = 1
        """
    val whereId = """ AND u.id = ?"""
    val whereName = """ AND u.first_name LIKE ? AND u.second_name LIKE ?"""
    val orderingClause = """ ORDER BY id"""

    @Transactional
    fun save(user: UserInternal) {
        with(user) {
            saveUser()
            saveCreds()
            saveBio()
            saveCity()
        }
    }

    fun UserInternal.saveCity() {
        // todo: возможно надо будет добавить нормализацию разных написаний/id регионов
        jdbcTemplate.update("INSERT INTO location (user_id, city) VALUES (?, ?)", id, city)
    }

    fun UserInternal.saveUser() {
        jdbcTemplate.update(
                "INSERT INTO users (id, first_name, second_name, age) VALUES (?, ?, ?, ?)",
                id,
                firstName,
                secondName,
                age
        )
    }

    fun UserInternal.saveCreds() {
        val encrypted = hash(password)
        jdbcTemplate.update("INSERT INTO user_credentials (user_id, bcrypt) VALUES (?, ?)", id, encrypted)
    }

    fun UserInternal.saveBio() {
        // todo: пока храним как есть единым тегом но мб потом понадобится список и поиск по ним
        val tagKeyholder = GeneratedKeyHolder()
        val ps = PreparedStatementCreator {
            val ps = it.prepareStatement("INSERT INTO tags (tag_type, tag_value) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)
            ps.setString(1, "bio")
            ps.setString(2, biography)
            ps
        }
        jdbcTemplate.update(ps, tagKeyholder)
        jdbcTemplate.update("INSERT INTO user_tags (tag_id, user_id) VALUES (?, ?)", tagKeyholder.key, id)
    }

    fun find(id: String): UserRow? {
        return try {
            val ps = PreparedStatementSetter {
                it.setString(1, id)
            }
            val query = userQuery + whereId
            return provideConnector().query(
                    query,
                    ps,
                    userMapper
            )[0]
        } catch (e: Exception) {
            logger.error("Failed to retrieve user {}", e)
            null
        }
    }

    fun find(firstName: String, secondName: String): List<UserRow>? {
        return try {
            val ps = PreparedStatementSetter {
                it.setString(1, wrapLike(firstName))
                it.setString(2, wrapLike(secondName))
            }
            val query = userQuery + whereName + orderingClause
            logger.info { query }
            provideConnector().query(
                    query,
                    ps,
                    userMapper
            )
        } catch (e: Exception) {
            logger.error("Failed to retrieve users {}", e)
            null
        }
    }

    private fun wrapLike(value: String): String {
        return "$value%"
    }

    fun login(id: String, password: String): LoginService.LoginState {
        val rm = SingleColumnRowMapper(ByteArray::class.java)
        val ps = PreparedStatementSetter {
            it.setString(1, id)
        }
        val bcrypt = jdbcTemplate.query("SELECT bcrypt FROM user_credentials WHERE user_id = ?", ps, rm)
        if (bcrypt.isEmpty()) {
            logger.warn { "User {} not found".format(id) }
            return LoginService.LoginState.NOT_FOUND
        }
        return if (verify(password, bcrypt[0])) {
            LoginService.LoginState.SUCCESS
        } else {
            LoginService.LoginState.NOT_ALLOWED
        }
    }

    fun provideConnector(): JdbcTemplate {
        if (featureService.isReplica()) {
            return jdbcReplicaTemplate
        } else {
            return jdbcTemplate
        }
    }

}
