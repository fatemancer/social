package info.hauu.highloadsocial.config

import com.mysql.jdbc.Driver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import javax.sql.DataSource


@Configuration
class DBConfig {

    @Bean
    fun source() : DataSource {
        val ds = SimpleDriverDataSource()
        ds.driver = Driver()
        ds.url = "jdbc:mysql://localhost:3306/social"
        // fixme: to properties
        ds.username = "user"
        ds.password = "password"
        return ds
    }
}