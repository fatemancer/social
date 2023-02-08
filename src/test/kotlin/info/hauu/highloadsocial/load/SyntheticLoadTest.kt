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
        var prev = 0
        for (i in 1..300000) {
            val result = restTemplate.exchange("http://localhost:16868/internal/load", HttpMethod.PUT, null, Int::class.java)
            if (result.body == -1) {
                println("${i - 1} : $prev")
                println("$i : $result")
            }
            prev = i
            Thread.sleep(5)
        }
    }

}
