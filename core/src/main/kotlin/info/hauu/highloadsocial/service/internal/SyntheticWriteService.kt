package info.hauu.highloadsocial.service.internal

import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.sql.Statement

private val logger = KotlinLogging.logger {}

@Controller
@RequestMapping("internal/load")
class SyntheticWriteService(val jdbcTemplate: JdbcTemplate) {

    // all abstractions are leaking for the sake of simplicity

    @PutMapping
    fun write(): ResponseEntity<Number> {
        val tagKeyholder = GeneratedKeyHolder()
        val integer = try {
            val query = "INSERT INTO numbers () VALUES ()"
            jdbcTemplate.update(
                    { it.prepareStatement(query, Statement.RETURN_GENERATED_KEYS) },
                    tagKeyholder
            )
            tagKeyholder.key
        } catch (e: Exception) {
            logger.error { e }
            -1
        }
        return ResponseEntity.ok(integer)
    }
}
