package info.hauu.highloadsocial.repository

import info.hauu.highloadsocial.model.api.PostRequest
import info.hauu.highloadsocial.model.domain.PostEntity
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.Statement

@Component
class PostRepository(
    val jdbcTemplate: JdbcTemplate,
) {
    val postMapper = DataClassRowMapper(PostEntity::class.java)

    @Transactional
    fun save(postRequest: PostRequest) {
        val keyHolder = GeneratedKeyHolder()
        val psPost = PreparedStatementCreator {
            val ps = it.prepareStatement("INSERT INTO posts (post_title, user_id) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)
            ps.setString(1, postRequest.title)
            ps.setString(2, postRequest.userId)
            ps
        }
        jdbcTemplate.update(psPost, keyHolder)

        val psContent = PreparedStatementCreator {
            val ps = it.prepareStatement("INSERT INTO posts_content (post_id, post) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)
            ps.setInt(1, keyHolder.key?.toInt()!!)
            ps.setString(2, postRequest.post)
            ps
        }
        jdbcTemplate.update(psContent)
    }

    fun delete(postId: Long, userId: String) {
        jdbcTemplate.update(
            "DELETE FROM posts WHERE id = ? and user_id = ?",
            postId,
            userId
        )
    }

    fun feed(userId: String, feedSize: Int): List<PostEntity> {
        return jdbcTemplate.query(
            """
                SELECT p.id as id, post_title as title, pc.post as post, p.user_id as author 
                FROM posts p
                JOIN posts_content pc ON p.id = pc.post_id
                JOIN friends f ON f.publisher_id = p.user_id
                WHERE f.subscriber_id = ?
                ORDER BY p.id DESC
                LIMIT ?
            """.trimIndent(),
            postMapper,
            userId,
            feedSize
        )
    }

    fun findById(postId: String): List<PostEntity>? {
        return jdbcTemplate.query(
            """
                SELECT p.id as id, post_title as title, pc.post as post, p.user_id as author 
                FROM posts p
                JOIN posts_content pc ON p.id = pc.post_id
                WHERE p.id = ?
            """.trimIndent(),
            postMapper,
            postId
        )
    }

    fun update(userId: String, postId: String, text: String) {
        jdbcTemplate.update(
            """
                UPDATE posts_content pc
                JOIN posts p on p.id = pc.post_id
                SET pc.post = ?
                WHERE p.user_id = ?
                AND p.id = ?
            """.trimIndent(),
            text,
            userId,
            postId
        )
    }

}