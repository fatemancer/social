package info.hauu.highloadsocial.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class PostRepository(
    val jdbcTemplate: JdbcTemplate,
) {

}