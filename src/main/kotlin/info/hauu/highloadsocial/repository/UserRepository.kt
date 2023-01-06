package info.hauu.highloadsocial.repository

import de.nycode.bcrypt.hash
import info.hauu.highloadsocial.model.UserInternal
import info.hauu.highloadsocial.model.UserRow
import mu.KotlinLogging
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {}

@Component
class UserRepository(val jdbcTemplate: JdbcTemplate) {

    val userMapper = DataClassRowMapper(UserRow::class.java)
    val userQuery = """"
        SELECT id, first_name, second_name, age, biography, city 
        FROM users AS u
        JOIN user_tags AS ut ON ut.user_id = u.id
        JOIN tags AS t ON t.id = ut.tag_id AND t.type = "bio"
        JOIN locations AS l ON l.user_id = u.id
        WHERE 1 = 1
        """
    val whereId = " AND id = ?"
    val whereName = " AND first_name = ? AND second_name = ?"

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
        val ps = PreparedStatementSetter {
            it.setString(1, "bio")
            it.setString(2, biography)
        }
        jdbcTemplate.update("INSERT INTO tags (tag_type, tag_value) VALUES (?, ?)", ps, tagKeyholder)
        jdbcTemplate.update("INSERT INTO user_tags (tag_id, user_id) VALUES (?, ?)", tagKeyholder.key, id)
    }

    fun find(id: String): UserRow? {
        return try {
            val ps = PreparedStatementSetter {
                it.setString(1, id)
            }
            return jdbcTemplate.query(
                userQuery + whereId,
                ps,
                userMapper
            )[0]
        } catch (e: Exception) {
            logger.error("Failed to retrieve user {}", e)
            null
        }
    }

    fun find(firstName: String, secondName: String): List<UserRow>? {
        val ps = PreparedStatementSetter {
            it.setString(1, firstName)
            it.setString(2, secondName)
        }
        val users = jdbcTemplate.query(
            userQuery + whereName,
            ps,
            userMapper
        )
    }

}