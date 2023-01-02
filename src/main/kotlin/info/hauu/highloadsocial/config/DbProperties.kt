package info.hauu.highloadsocial.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@ConstructorBinding
class DbProperties {
    lateinit var username: String
    lateinit var password: String
    lateinit var url: String

    override fun toString(): String {
        return "DbProperties(username='$username', password='$password', url='$url')"
    }

}

