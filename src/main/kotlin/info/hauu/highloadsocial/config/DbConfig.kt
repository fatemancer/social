package info.hauu.highloadsocial.config

import com.mysql.jdbc.Driver
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import javax.sql.DataSource

private val logger = KotlinLogging.logger {}
@Configuration
class DbConfig(val dbProperties: DbProperties) {
    @Bean
    @Primary
    fun source() : DataSource {
        val ds = SimpleDriverDataSource()
        ds.apply {
            url = dbProperties.url
            username = dbProperties.username
            password = dbProperties.password
            ds.driver = Driver()
        }.also { logger.info { "DB connected with ${dbProperties.host()}" } }
        return ds
    }

    // maybe this can be designed more elegantly?
    @Bean
    fun replicaSource() : DataSource {
        val ds = SimpleDriverDataSource()
        ds.apply {
            url = dbProperties.replicaUrl
            username = dbProperties.username
            password = dbProperties.password
            ds.driver = Driver()
        }.also { logger.info { "DB connected with ${dbProperties.replica()}" } }
        return ds
    }

    @Bean
    @Primary
    fun jdbcTemplate() : JdbcTemplate {
        return JdbcTemplate(source())
    }

    @Bean
    fun jdbcReplicaTemplate() : JdbcTemplate {
        return JdbcTemplate(replicaSource())
    }
}
