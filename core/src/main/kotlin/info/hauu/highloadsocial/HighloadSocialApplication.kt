package info.hauu.highloadsocial

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["info.hauu.highloadsocial", "org.openapi", "info.hauu.highloadsocial.config"])
@ConfigurationPropertiesScan(basePackages = ["info.hauu.highloadsocial.properties"])
class HighloadSocialApplication

fun main(args: Array<String>) {
    runApplication<HighloadSocialApplication>(*args)
}
