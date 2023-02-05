package info.hauu.highloadsocial.load

import info.hauu.highloadsocial.HighloadSocialApplication
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForObject
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.stream.Stream

@SpringBootTest
@ContextConfiguration(classes = [HighloadSocialApplication::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWebClient
class QueryAmountTest(
        @Autowired val jdbcTemplate: JdbcTemplate,
) {
    lateinit var names: List<Pair<String, String>>;
    var restTemplate: TestRestTemplate = TestRestTemplate()
    var executorService: ExecutorService = Executors.newFixedThreadPool(128)

    @BeforeAll
    fun init() {
        names = jdbcTemplate.queryForList("SELECT DISTINCT first_name, second_name FROM users\n" + "ORDER BY rand()\n" + "LIMIT 50000;").map { Pair(it["first_name"].toString(), it["second_name"].toString()) }
    }

    @ParameterizedTest
    @MethodSource("provideLight")
    fun `Async run & wait for some queries`(amount: Int) {
        val tasks: MutableList<Callable<Any>> = mutableListOf()
        names.subList(0, amount).forEach { n ->
            val url = "http://localhost:16868/user/search?first_name=${n.first}&last_name=${n.second}"
            tasks.add {
                restTemplate.getForObject<String>(url)
            }
        }
        executorService.invokeAll(tasks)
    }

    @ParameterizedTest
    @MethodSource("provideMedium")
    fun `Async run & wait for some more queries`(amount: Int) {
        val tasks: MutableList<Callable<Any>> = mutableListOf()
        names.subList(0, amount).forEach { n ->
            val url = "http://localhost:16868/user/search?first_name=${n.first}&last_name=${n.second}"
            tasks.add {
                restTemplate.getForObject<String>(url)
            }
        }
        executorService.invokeAll(tasks)
    }

    @ParameterizedTest
    @MethodSource("provideHeavy")
    fun `Async run & wait for lots of queries`(amount: Int) {
        val tasks: MutableList<Callable<Any>> = mutableListOf()
        names.subList(0, amount).forEach { n ->
            val url = "http://localhost:16868/user/search?first_name=${n.first}&last_name=${n.second}"
            tasks.add {
                restTemplate.getForObject<String>(url)
            }
        }
        executorService.invokeAll(tasks)
    }

//    @ParameterizedTest
//    @MethodSource("provideSuperHeavy")
//    fun `Async run & wait for even more queries`(amount: Int) {
//        val tasks: MutableList<Callable<Any>> = mutableListOf()
//        names.subList(0, amount).forEach { n ->
//            val url = "http://localhost:16868/user/search?first_name=${n.first}&last_name=${n.second}"
//            tasks.add {
//                restTemplate.getForObject<String>(url)
//            }
//        }
//        executorService.invokeAll(tasks)
//    }

    companion object {
        @JvmStatic
        fun provideLight(): Stream<Arguments> {
            return Stream.of(
                    Arguments.of(1),
                    Arguments.of(10),
                    Arguments.of(150)
            );
        }

        @JvmStatic
        fun provideMedium(): Stream<Arguments> {
            return Stream.of(
                    Arguments.of(100),
                    Arguments.of(250),
                    Arguments.of(750)
            );
        }

        @JvmStatic
        fun provideHeavy(): Stream<Arguments> {
            return Stream.of(
                    Arguments.of(2000),
                    Arguments.of(5000),
                    Arguments.of(10000)
            );
        }
        @JvmStatic
        fun provideSuperHeavy(): Stream<Arguments> {
            return Stream.of(
                    Arguments.of(10000),
                    Arguments.of(20000),
                    Arguments.of(50000)
            );
        }
    }

}
