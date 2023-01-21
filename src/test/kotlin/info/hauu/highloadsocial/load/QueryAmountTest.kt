package info.hauu.highloadsocial.load

import info.hauu.highloadsocial.ungzip
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootTest
class QueryAmountTest(
    @Autowired
    val jdbcTemplate: JdbcTemplate
) {

    @BeforeAll
    fun init() {
//        ungzip()
    }

    @Test
    fun `1 query`() {

    }

}