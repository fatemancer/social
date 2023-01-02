package info.hauu.highloadsocial.config

import com.mysql.jdbc.Driver
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import javax.sql.DataSource

private val logger = KotlinLogging.logger {}
@Configuration
class DbConfig(val dbProperties: DbProperties) {
    @Bean
    fun source() : DataSource {
        val ds = SimpleDriverDataSource()
        ds.apply {
            url = dbProperties.url
            username = dbProperties.username
            password = dbProperties.password
            ds.driver = Driver()
        }.also { logger.info { "DB connected with $dbProperties" } }
        return ds
    }
}