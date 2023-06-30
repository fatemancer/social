package info.hauu.highloadsocial.repository

import info.hauu.highloadsocial.model.domain.DialogEntity
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class DialogRepository(
    val jdbcTemplate: JdbcTemplate
) {
    fun retrieve(initiatorId: String, receiverId: String): List<DialogEntity> {
        return jdbcTemplate.query("""
            SELECT initiator_id, receiver_id, text 
            FROM dialog d
            WHERE author_id = ? 
            AND receiver_id = ?
            ORDER BY id DESC
        """.trimIndent(),
            DataClassRowMapper(),
            initiatorId,
            receiverId
        )
    }

    fun write(initiatorId: String, receiverId: String, text: String): Unit? {
        jdbcTemplate.update(
            """
                INSERT INTO dialog
                (initiator_id, receiver_id, text)
                VALUES 
                (?, ?, ?)
            """.trimIndent(),
            initiatorId,
            receiverId,
            text
        )
        return null
    }
}