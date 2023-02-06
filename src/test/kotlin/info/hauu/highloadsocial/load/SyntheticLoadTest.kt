package info.hauu.highloadsocial.load

import info.hauu.highloadsocial.HighloadSocialApplication
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [HighloadSocialApplication::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWebClient
class SyntheticLoadTest {
    var restTemplate: TestRestTemplate = TestRestTemplate()

    @Test
    fun `Run & wait for some queries`() {
        for (i in 1..300) {
            val result = restTemplate.exchange("http://localhost:16868/internal/load", HttpMethod.PUT, null, Int::class.java)
            println("$i : $result")
            Thread.sleep(500)
        }
    }

}
