package info.hauu.highloadsocial.load

import info.hauu.highloadsocial.HighloadSocialApplication
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [HighloadSocialApplication::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWebClient
class SyntheticLoadTest(
        @Autowired val jdbcTemplate: JdbcTemplate,
) {
    lateinit var names: List<Pair<String, String>>;
    var restTemplate: TestRestTemplate = TestRestTemplate()

    @Test
    fun `Run & wait for some queries`() {
        for (i in 1..100) {
            val result = restTemplate.put("http://localhost:16868/internal/load", null)
            println(result)
            Thread.sleep(200)
        }
    }

}
