package info.hauu.highloadsocial.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.SingleColumnRowMapper
import org.springframework.stereotype.Service


@Service
class FriendRepository(val jdbcTemplate: JdbcTemplate) {
    fun getSubscribers(authorId: String): List<String> {
        return jdbcTemplate.query(
            """
                SELECT subscriber_id FROM friends
                WHERE publisher_id = ?
            """.trimIndent(),
            SingleColumnRowMapper<String>(),
            authorId
        )
    }
}